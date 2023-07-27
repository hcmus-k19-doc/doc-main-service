package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestGetDto;
import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestPostDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.ReturnRequestNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.DocumentReminderRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRoleRepository;
import edu.hcmus.doc.mainservice.repository.ReturnRequestRepository;
import edu.hcmus.doc.mainservice.repository.TransferHistoryRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.ReturnRequestService;
import edu.hcmus.doc.mainservice.util.mapper.ReturnRequestMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ReturnRequestServiceImpl implements ReturnRequestService {

  private final ReturnRequestRepository returnRequestRepository;

  private final UserRepository userRepository;

  private final IncomingDocumentRepository incomingDocumentRepository;

  private final OutgoingDocumentRepository outgoingDocumentRepository;

  private final ReturnRequestMapper returnRequestMapper;

  private final ProcessingUserRepository processingUserRepository;

  private final ProcessingUserRoleRepository processingUserRoleRepository;

  private final TransferHistoryRepository transferHistoryRepository;

  private final DocumentReminderRepository documentReminderRepository;

  @Override
  public List<ReturnRequestGetDto> getReturnRequestsByDocumentId(Long documentId,
      ProcessingDocumentTypeEnum type) {
    return returnRequestRepository.getReturnRequestsByDocumentId(documentId, type).stream()
        .map(returnRequest -> returnRequestMapper.toReturnRequestGetDto(returnRequest, type))
        .toList();
  }

  @Override
  public ReturnRequestGetDto getReturnRequestById(Long returnRequestId,
      ProcessingDocumentTypeEnum type) {
    ReturnRequest returnRequest = returnRequestRepository.getReturnRequestById(returnRequestId)
        .orElseThrow(() -> new ReturnRequestNotFoundException(
            ReturnRequestNotFoundException.RETURN_REQUEST_NOT_FOUND));
    return returnRequestMapper.toReturnRequestGetDto(returnRequest, type);
  }

  @Override
  public List<Long> createReturnRequest(ReturnRequestPostDto returnRequestDto) {
    List<ReturnRequest> returnRequests = new ArrayList<>();
    ProcessingDocumentTypeEnum type = returnRequestDto.getType();
    // validate document, if type is INCOMING then check in incoming document table
    if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      returnRequestDto.getDocumentIds().forEach(documentId -> {
        ReturnRequest returnRequest = new ReturnRequest();
        // validate users
        validateUsers(returnRequestDto, returnRequest);
        // validate document
        incomingDocumentRepository
            .findById(documentId)
            .ifPresentOrElse(returnRequest::setIncomingDocument,
                () -> new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND));
        returnRequest.setReason(returnRequestDto.getReason());
        returnRequest.setStatus(ExtendRequestStatus.APPROVED);
        returnRequests.add(returnRequest);
      });
    } else if (type == ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT) {
      returnRequestDto.getDocumentIds().forEach(documentId -> {
        ReturnRequest returnRequest = new ReturnRequest();
        // validate users
        validateUsers(returnRequestDto, returnRequest);
        // validate document
        outgoingDocumentRepository.findById(documentId)
            .ifPresentOrElse(returnRequest::setOutgoingDocument,
                () -> new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND));
        returnRequest.setReason(returnRequestDto.getReason());
        returnRequest.setStatus(ExtendRequestStatus.APPROVED);
        returnRequests.add(returnRequest);
      });
    }
    // save to DB
    returnRequestRepository.saveAll(returnRequests);

    // create new history transfer record
    returnRequests.forEach(returnRequest -> {
      TransferHistory transferHistory = createTransferHistory(returnRequest, type);
      transferHistoryRepository.save(transferHistory);
    });

    // delete records in processing user and processing user role
    returnRequestDto.getDocumentIds().forEach(documentId -> {
      List<ProcessingUser> processingUsers = processingUserRepository.findByDocumentIdAndStep(
          documentId, returnRequestDto.getStep(), type);

      processingUsers.forEach(processingUser -> {
        ProcessingUserRole processingUserRole = processingUserRoleRepository.findByProcessingUserId(
            processingUser.getId());

        processingUserRoleRepository.delete(processingUserRole);
        documentReminderRepository.deleteByProcessingUserId(processingUser.getId());
        processingUserRepository.delete(processingUser);
      });

//      processingUserRepository.deleteAll(processingUsers);
    });

    return returnRequests.stream().map(ReturnRequest::getId).toList();
  }

  private void validateUsers(ReturnRequestPostDto returnRequestDto, ReturnRequest returnRequest) {
    // validate current processing user
    userRepository.findById(returnRequestDto.getCurrentProcessingUserId())
        .ifPresentOrElse(returnRequest::setCurrentProcessingUser,
            () -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

    // validate previous processing user
    userRepository.findById(returnRequestDto.getPreviousProcessingUserId())
        .ifPresentOrElse(returnRequest::setPreviousProcessingUser,
            () -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  private TransferHistory createTransferHistory(ReturnRequest returnRequest, ProcessingDocumentTypeEnum type) {
    TransferHistory transferHistory = new TransferHistory();
    transferHistory.setReturnRequest(returnRequest);
    transferHistory.setSender(returnRequest.getCurrentProcessingUser());
    transferHistory.setReceiver(returnRequest.getPreviousProcessingUser());
    if (type == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      transferHistory.setIncomingDocumentIds(List.of(returnRequest.getIncomingDocument().getId()));
    } else if (type == ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT) {
      transferHistory.setOutgoingDocumentIds(List.of(returnRequest.getOutgoingDocument().getId()));
    }
    return transferHistory;
  }
}
