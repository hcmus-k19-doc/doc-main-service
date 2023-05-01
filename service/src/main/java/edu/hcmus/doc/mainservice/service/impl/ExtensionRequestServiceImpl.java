package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.ExtensionRequestNotFoundException.EXTENSION_REQUEST_NOT_FOUND;
import static edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException.PROCESSING_DOCUMENT_NOT_FOUND;

import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.ExtensionRequestStatus;
import edu.hcmus.doc.mainservice.model.exception.ExtensionRequestNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.repository.ExtensionRequestRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.ExtensionRequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ExtensionRequestServiceImpl implements ExtensionRequestService {

  private final ExtensionRequestRepository extensionRequestRepository;

  private final ProcessingDocumentRepository processingDocumentRepository;

  @Override
  public List<ExtensionRequest> getCurrUserExtensionRequests() {
    return extensionRequestRepository.getExtensionRequestsByUsername(SecurityUtils.getCurrentUser().getUsername());
  }

  @Override
  public Long createExtensionRequest(Long processingDocId, ExtensionRequest entity) {
    processingDocumentRepository.findById(processingDocId)
        .ifPresentOrElse(entity::setProcessingDoc,
            () -> {
              throw new ProcessingDocumentNotFoundException(PROCESSING_DOCUMENT_NOT_FOUND);
            });
    entity.setStatus(ExtensionRequestStatus.PENDING);
    return extensionRequestRepository.save(entity).getId();
  }

  @Override
  public Long validateExtensionRequest(Long id, ExtensionRequestStatus validateCode) {
    ExtensionRequest extensionRequest = extensionRequestRepository
        .findById(id)
        .orElseThrow(() -> new ExtensionRequestNotFoundException(EXTENSION_REQUEST_NOT_FOUND));

    if (validateCode == ExtensionRequestStatus.APPROVED) {
      extensionRequest.getProcessingDoc()
          .setProcessingDuration(extensionRequest.getExtendedUntil());
    } else {
      extensionRequest.getProcessingDoc()
          .setProcessingDuration(extensionRequest.getOldExpiredDate());
    }

    extensionRequest.setStatus(validateCode);
    return extensionRequestRepository.save(extensionRequest).getId();
  }
}
