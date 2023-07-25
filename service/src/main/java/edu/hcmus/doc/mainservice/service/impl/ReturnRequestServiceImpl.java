package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException.PROCESSING_DOCUMENT_NOT_FOUND;

import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestGetDto;
import edu.hcmus.doc.mainservice.model.dto.ReturnRequest.ReturnRequestPostDto;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.enums.ExtendRequestStatus;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.ProcessingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ReturnRequestRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.service.ReturnRequestService;
import edu.hcmus.doc.mainservice.util.mapper.ReturnRequestMapper;
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
    return returnRequestMapper.toReturnRequestGetDto(
        returnRequestRepository.getReturnRequestById(returnRequestId), type);
  }

  @Override
  public Long createReturnRequest(ReturnRequestPostDto returnRequestDto) {
    ReturnRequest returnRequest = new ReturnRequest();

    // validate current processing user
    userRepository.findById(returnRequestDto.getCurrentProcessingUserId())
        .ifPresentOrElse(returnRequest::setCurrentProcessingUser,
            () -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

    // validate previous processing user
    userRepository.findById(returnRequestDto.getPreviousProcessingUserId())
        .ifPresentOrElse(returnRequest::setPreviousProcessingUser,
            () -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

    // validate document, if type is INCOMING then check in incoming document table
    if (returnRequestDto.getType() == ProcessingDocumentTypeEnum.INCOMING_DOCUMENT) {
      incomingDocumentRepository.findById(returnRequestDto.getDocumentId())
          .ifPresentOrElse(returnRequest::setIncomingDocument,
              () -> new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND));
    } else if (returnRequestDto.getType() == ProcessingDocumentTypeEnum.OUTGOING_DOCUMENT) {
      outgoingDocumentRepository.findById(returnRequestDto.getDocumentId())
          .ifPresentOrElse(returnRequest::setOutgoingDocument,
              () -> new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND));
    }

    returnRequest.setReason(returnRequestDto.getReason());
    returnRequest.setStatus(ExtendRequestStatus.APPROVED);
    return returnRequestRepository.save(returnRequest).getId();
  }
}
