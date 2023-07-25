package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QIncomingDocument.incomingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument.outgoingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingDocument.processingDocument;
import static edu.hcmus.doc.mainservice.model.entity.QProcessingUser.processingUser;

import com.querydsl.core.BooleanBuilder;
import edu.hcmus.doc.mainservice.model.entity.QExtendRequest;
import edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.QReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomReturnRequestRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomReturnRequestRepositoryImpl extends DocAbstractCustomRepository<ReturnRequest>
    implements CustomReturnRequestRepository {

  private static final QReturnRequest qReturnRequest = QReturnRequest.returnRequest;

  @Override
  public List<ReturnRequest> getReturnRequestsByDocumentId(Long documentId,
      ProcessingDocumentTypeEnum type) {
    BooleanBuilder whereBuilder = new BooleanBuilder();
    if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      whereBuilder.and(incomingDocument.id.eq(documentId));
    } else {
      whereBuilder.and(outgoingDocument.id.eq(documentId));
    }

    return selectFrom(qReturnRequest)
        .where(whereBuilder)
        .fetch();
  }

  @Override
  public ReturnRequest getReturnRequestById(Long returnRequestId) {
    return selectFrom(qReturnRequest)
        .where(qReturnRequest.id.eq(returnRequestId))
        .fetchFirst();
  }
}
