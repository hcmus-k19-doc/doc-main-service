package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.ExtensionRequestNotFoundException.EXTENSION_REQUEST_NOT_FOUND;
import static edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException.PROCESSING_DOCUMENT_NOT_FOUND;

import edu.hcmus.doc.mainservice.model.entity.ExtendRequest;
import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import edu.hcmus.doc.mainservice.model.exception.ExtensionRequestNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.ExtensionRequestRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.ExtendRequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ExtendRequestServiceImpl implements ExtendRequestService {

  private final ExtensionRequestRepository extensionRequestRepository;

  private final ProcessingUserRepository processingUserRepository;
  private final UserRepository userRepository;

  @Override
  public List<ExtendRequest> getExtensionRequestsByUsername(String username) {
    return extensionRequestRepository.getExtensionRequestsByUsername(username);
  }

  @Override
  public Long createExtensionRequest(Long processingDocId, ExtendRequest extendRequest) {
    processingUserRepository.findById(processingDocId)
        .ifPresentOrElse(processingUser -> {
              extendRequest.setProcessingUser(processingUser);
              extendRequest.setStatus(ExtendRequestStatus.PENDING);
            },
            () -> {
              throw new ProcessingDocumentNotFoundException(PROCESSING_DOCUMENT_NOT_FOUND);
            });
    return extensionRequestRepository.save(extendRequest).getId();
  }

  @Override
  public Long validateExtensionRequest(Long id, Long validatorId, ExtendRequestStatus status) {
    ExtendRequest extendRequest = extensionRequestRepository
        .findById(id)
        .orElseThrow(() -> new ExtensionRequestNotFoundException(EXTENSION_REQUEST_NOT_FOUND));

    if (status == ExtendRequestStatus.APPROVED) {
      extendRequest.getProcessingUser()
          .setProcessingDuration(extendRequest.getNewDeadline());
    } else {
      extendRequest.getProcessingUser()
          .setProcessingDuration(extendRequest.getOldDeadline());
    }

    extendRequest.setValidatedBy(userRepository
        .findById(validatorId)
        .orElseThrow(UserNotFoundException::new));
    extendRequest.setStatus(status);
    return extensionRequestRepository.save(extendRequest).getId();
  }
}
