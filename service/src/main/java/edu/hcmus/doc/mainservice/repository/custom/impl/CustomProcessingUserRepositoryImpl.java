package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.ASSIGNEE;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.COLLABORATOR;
import static edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum.REPORTER;

import com.querydsl.core.BooleanBuilder;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QIncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.QProcessingUserRole;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CustomProcessingUserRepositoryImpl
    extends DocAbstractCustomRepository<ProcessingUser>
    implements CustomProcessingUserRepository {

  private static final QProcessingUser qProcessingUser = QProcessingUser.processingUser;
  private static final QProcessingUserRole qProcessingUserRole = QProcessingUserRole.processingUserRole;

  @Override
  public List<ProcessingUser> findAllByUserIdAndProcessingDocumentId(Long userId,
      Long processingDocumentId) {
    return selectFrom(processingUser)
        .where(processingUser.user.id.eq(userId)
            .and(processingUser.processingDocument.id.eq(processingDocumentId)))
        .fetch();
  }

  @Override
  public boolean isProcessAtStep(Long incomingDocumentId, int step) {
    return selectFrom(processingUser)
        .select(processingUser.id)
        .leftJoin(qProcessingUserRole)
        .on(qProcessingUser.id.eq(qProcessingUserRole.processingUser.id))
        .where(processingUser.processingDocument.incomingDoc.id.eq(incomingDocumentId)
            .and(processingUser.user.id.eq(SecurityUtils.getCurrentUser().getId()))
            .and(processingUser.step.eq(step))
            .and(qProcessingUserRole.role.eq(ASSIGNEE)))
        .fetchFirst() != null;
  }

  @Override
  public Optional<LocalDate> getDateExpired(Long incomingDocumentId, Long userId,
      DocSystemRoleEnum userRole
      , Boolean isAnyRole) {
    QProcessingDocument qProcessingDocument = new QProcessingDocument(
        qProcessingUser.processingDocument.getMetadata().getName());
    QProcessingUserRole qProcessingUserRole = QProcessingUserRole.processingUserRole;
    QIncomingDocument incomingDocument = new QIncomingDocument(
        qProcessingDocument.incomingDoc.getMetadata().getName());

    BooleanBuilder whereBuilder = new BooleanBuilder();
    if (Boolean.TRUE.equals(isAnyRole)) {
      if (userRole.equals(DocSystemRoleEnum.VAN_THU)) {
        // If isAnyRole is true and userRole is VANTHU, select roles: ASSIGNEE, COLLABORATOR, and REPORTER
        whereBuilder.and(qProcessingUserRole.role.in(ASSIGNEE, COLLABORATOR, REPORTER));
      } else {
        // If isAnyRole is true and userRole is not VANTHU, select roles: ASSIGNEE, COLLABORATOR
        whereBuilder.and(qProcessingUserRole.role.in(ASSIGNEE, COLLABORATOR));
      }
    } else {
      // If isAnyRole is false, select role: ASSIGNEE
      whereBuilder.and(qProcessingUserRole.role.eq(ASSIGNEE));
    }

    return Optional.ofNullable(selectFrom(qProcessingUser)
        .select(qProcessingUser.processingDuration)
        .innerJoin(qProcessingDocument)
        .on(qProcessingDocument.id.eq(qProcessingUser.processingDocument.id)
            .and(qProcessingUser.user.id.eq(userId)))
        .innerJoin(qProcessingUserRole)
        .on(qProcessingUserRole.processingUser.id.eq(qProcessingUser.id)
            .and(whereBuilder))
        .innerJoin(qProcessingDocument.incomingDoc, incomingDocument)
        .on(incomingDocument.id.eq(incomingDocumentId))
        .fetchFirst());
  }

  @Override
  public List<ProcessingUser> findByUserIdAndProcessingDocumentIdWithRole(Long userId,
      Long processingDocumentId) {
    return selectFrom(processingUser)
        .leftJoin(qProcessingUserRole)
        .on(qProcessingUser.id.eq(qProcessingUserRole.processingUser.id))
        .where(processingUser.user.id.eq(userId)
            .and(processingUser.processingDocument.id.eq(processingDocumentId)))
        .orderBy(qProcessingUserRole.role.asc())
        .fetch();
  }

  @Override
  public ProcessingUser findByUserIdAndProcessingDocumentIdAndRoleAssignee(Long userId,
      Long processingDocumentId) {
    return selectFrom(processingUser)
        .leftJoin(qProcessingUserRole)
        .on(qProcessingUser.id.eq(qProcessingUserRole.processingUser.id))
        .where(processingUser.user.id.eq(userId)
            .and(processingUser.processingDocument.id.eq(processingDocumentId))
            .and(qProcessingUserRole.role.eq(ASSIGNEE)))
        .fetchFirst();
  }
}
