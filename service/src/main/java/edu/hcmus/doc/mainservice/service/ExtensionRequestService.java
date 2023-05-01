package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import edu.hcmus.doc.mainservice.model.enums.ExtensionRequestStatus;
import java.util.List;

public interface ExtensionRequestService {

  List<ExtensionRequest> getCurrUserExtensionRequests();

  Long createExtensionRequest(Long processingDocId, ExtensionRequest entity);

  Long validateExtensionRequest(Long id, ExtensionRequestStatus validateCode);
}
