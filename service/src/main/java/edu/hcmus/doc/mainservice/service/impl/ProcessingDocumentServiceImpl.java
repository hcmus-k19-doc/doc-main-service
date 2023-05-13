package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.getStep;

import edu.hcmus.doc.mainservice.model.dto.ElasticSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.ProcessingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.ValidateTransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessingDocumentServiceImpl implements ProcessingDocumentService {

  private final ProcessingDocumentRepository processingDocumentRepository;

  private final AsyncRabbitTemplate asyncRabbitTemplate;

  private final UserRepository userRepository;

  @Value("${spring.rabbitmq.template.exchange}")
  private String exchange;

  @Value("${spring.rabbitmq.template.routing-key}")
  private String routingkey;

  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return processingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    return processingDocumentRepository.getTotalPages(searchCriteriaDto, limit);
  }

  @Override
  public List<ProcessingDocument> searchProcessingDocuments(
      SearchCriteriaDto searchCriteriaDto,
      long offset,
      long limit
  ) {
    return processingDocumentRepository.searchByCriteria(searchCriteriaDto, offset, limit);
  }

  @Override
  public ProcessingDocumentSearchResultDto searchProcessingDocumentsByElasticSearch(
      ElasticSearchCriteriaDto elasticSearchCriteriaDto, long offset, long limit)
      throws ExecutionException, InterruptedException {
    ProcessingDocumentSearchResultDto processingDocumentSearchResultDto = new ProcessingDocumentSearchResultDto();
    RabbitConverterFuture<List<IncomingDocumentSearchResultDto>> rabbitConverterFuture = asyncRabbitTemplate.convertSendAndReceiveAsType(
        exchange,
        routingkey,
        elasticSearchCriteriaDto,
        new ParameterizedTypeReference<>() {
        }
    );
    rabbitConverterFuture.addCallback(
        new ListenableFutureCallback<>() {
          @Override
          public void onFailure(Throwable ex) {
            throw new RuntimeException(ex);
          }

          @Override
          public void onSuccess(List<IncomingDocumentSearchResultDto> result) {
          }
        });

    List<IncomingDocumentSearchResultDto> result = rabbitConverterFuture.get();
    processingDocumentSearchResultDto.setProcessingDocuments(
        processingDocumentRepository.findProcessingDocumentsByElasticSearchResult(result, offset,
            limit));
    processingDocumentSearchResultDto.setTotalElements(Objects.requireNonNull(result).size());
    if (result.size() % limit == 0) {
      processingDocumentSearchResultDto.setTotalPages(result.size() / limit);
    } else {
      processingDocumentSearchResultDto.setTotalPages(result.size() / limit + 1);
    }
    return processingDocumentSearchResultDto;
  }

  @Override
  public Boolean isUserWorkingOnDocumentWithRole(GetTransferDocumentDetailRequest request) {
    GetTransferDocumentDetailResponse detail = processingDocumentRepository.findTransferDocumentDetail(
        request);
    return detail != null;
  }

  @Override
  public ValidateTransferDocDto validateTransferDocument(TransferDocDto transferDocDto) {

    User currentUser = SecurityUtils.getCurrentUser();
    User reporter = userRepository.findById(transferDocDto.getReporterId())
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

    User assignee = userRepository.findById(transferDocDto.getAssigneeId())
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

    List<User> collaborators = userRepository.findAllById(
        Objects.requireNonNull(transferDocDto.getCollaboratorIds()));

    int step = getStep(reporter, assignee);

    if (transferDocDto.getIsTransferToSameLevel()) {
      // neu la van thu, khong can kiem tra
      if (currentUser.getRole() != DocSystemRoleEnum.VAN_THU) {
        // kiem tra xem current user co phai la assignee cua van ban nay khong
        // kiem tra xem reporter hay collaborator co dang xu ly van ban nay khong
      }
    } else {
        // kiem tra xem tai step hien tai, assignee, reporter, collaborator co dang xu ly van ban nay khong
    }
    return null;
  }
}
