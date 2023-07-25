package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.util.List;

public interface CustomReturnRequestRepository {

  List<ReturnRequest> getReturnRequestsByDocumentId(Long documentId, ProcessingDocumentTypeEnum type);
  ReturnRequest getReturnRequestById(Long returnRequestId);
}
