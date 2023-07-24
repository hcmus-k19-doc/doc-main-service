package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomProcessingUserRepository {

  List<ProcessingUser> findAllByUserIdAndProcessingDocumentId(Long userId, Long processingDocumentId);

  boolean isProcessAtStep(Long incomingDocumentId, int step);

  Optional<LocalDate> getDateExpired(Long incomingDocumentId, Long userId, DocSystemRoleEnum userRole, Boolean isAnyRole);

  Optional<String> getDateExpiredV2(Long incomingDocumentId, Long userId, DocSystemRoleEnum userRole, Boolean isAnyRole);

  List<ProcessingUser> findByUserIdAndProcessingDocumentIdWithRole(Long userId, Long processingDocumentId);

  ProcessingUser findByUserIdAndProcessingDocumentIdAndRoleAssignee(Long userId, Long processingDocumentId);
}
