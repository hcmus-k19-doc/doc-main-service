package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;

import edu.hcmus.doc.mainservice.model.entity.ExtendRequest;
import edu.hcmus.doc.mainservice.model.entity.QExtendRequest;
import edu.hcmus.doc.mainservice.repository.custom.CustomExtensionRequestRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomExtensionRequestRepositoryImpl
    extends DocAbstractCustomRepository<ExtendRequest>
    implements CustomExtensionRequestRepository {

  private static final QExtendRequest qExtendRequest = QExtendRequest.extendRequest;

  @Override
  public List<ExtendRequest> getExtensionRequestsByUsername(String username) {
    return selectFrom(qExtendRequest)
        .innerJoin(qExtendRequest.processingUser, processingUser)
        .fetchJoin()
        .innerJoin(processingUser.processingDocument, processingDocument)
        .fetchJoin()
        .innerJoin(processingDocument.incomingDoc, incomingDocument)
        .fetchJoin()
        .where(qExtendRequest.createdBy.eq(username))
        .orderBy(qExtendRequest.createdDate.desc())
        .fetch();
  }
}
