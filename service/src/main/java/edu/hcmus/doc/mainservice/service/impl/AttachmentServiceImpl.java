package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.FileDto;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import edu.hcmus.doc.mainservice.model.exception.DocMainServiceRuntimeException;
import edu.hcmus.doc.mainservice.repository.AttachmentRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.OutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class AttachmentServiceImpl implements AttachmentService {

  @Value("${spring.rabbitmq.template.exchange}")
  private String exchange;

  @Value("${spring.rabbitmq.template.attachment-routing-key}")
  private String routingkey;

  @Value("${spring.rabbitmq.template.s3-attachment-routing-key}")
  private String s3AttachmentRoutingKey;

  private final AttachmentRepository attachmentRepository;

  private final IncomingDocumentRepository incomingDocumentRepository;

  private final OutgoingDocumentRepository outgoingDocumentRepository;

  private final AsyncRabbitTemplate asyncRabbitTemplate;

  private final AttachmentMapperDecorator attachmentMapperDecorator;

  @Override
  public List<AttachmentDto> getAttachmentsByIncomingDocId(Long incomingDocId) {
    return attachmentRepository.getAttachmentsByIncomingDocId(incomingDocId).stream().map(
        attachmentMapperDecorator::toDto).toList();
  }

  @SneakyThrows
  @Override
  public List<AttachmentDto> saveAttachmentsByIncomingDocId(AttachmentPostDto attachmentPostDto) {
    if (CollectionUtils.isEmpty(attachmentPostDto.getAttachments())) {
      return List.of();
    }

    attachmentPostDto.setParentFolder(ParentFolderEnum.ICD);
    RabbitConverterFuture<List<FileDto>> rabbitConverterFuture = asyncRabbitTemplate
        .convertSendAndReceiveAsType(exchange, routingkey, attachmentPostDto,
            new ParameterizedTypeReference<>() {
            }
        );

    rabbitConverterFuture.addCallback(
        new ListenableFutureCallback<>() {
          @Override
          public void onFailure(Throwable ex) {
            log.error("Error when saving attachments", ex);
            throw new DocMainServiceRuntimeException("Error when saving attachments", ex);
          }

          @Override
          public void onSuccess(List<FileDto> result) {
            log.info("Save attachments successfully");
          }
        }
    );

    // save file info to db
    List<FileDto> fileDtos = rabbitConverterFuture.get();

    List<AttachmentDto> attachmentDtos = Objects.requireNonNull(fileDtos).stream()
        .map(fileDto -> attachmentMapperDecorator.convertFileDtoToAttachmentDto(
            attachmentPostDto.getDocId(), fileDto)).toList();

    incomingDocumentRepository.findById(attachmentPostDto.getDocId())
        .ifPresent(incomingDoc ->
            attachmentDtos.stream().map(attachmentMapperDecorator::toEntity).forEach(attachment -> {
              attachment.setIncomingDoc(incomingDoc);
              attachmentRepository.save(attachment);
            }));

    return attachmentDtos;
  }

    @SneakyThrows
    @Override
    public List<AttachmentDto> saveAttachmentsByOutgoingDocId(AttachmentPostDto attachmentPostDto) {
        if (attachmentPostDto.getAttachments().size() == 0) {
            return List.of();
        }

        RabbitConverterFuture<List<FileDto>> rabbitConverterFuture = asyncRabbitTemplate
                .convertSendAndReceiveAsType(exchange, routingkey, attachmentPostDto,
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
                    public void onSuccess(List<FileDto> result) {
                    }
                }
        );

        // save file info to db
        List<FileDto> fileDtos = rabbitConverterFuture.get();

        List<AttachmentDto> attachmentDtos = Objects.requireNonNull(fileDtos).stream()
                .map(fileDto -> attachmentMapperDecorator.convertFileDtoToAttachmentDto(
                        attachmentPostDto.getDocId(), fileDto)).toList();

        outgoingDocumentRepository.findById(attachmentPostDto.getDocId())
                .ifPresent(doc -> {
                    attachmentDtos.stream().map(attachmentMapperDecorator::toEntity).forEach(attachment -> {
                        attachment.setOutgoingDocument(doc);
                        attachmentRepository.save(attachment);
                    });
                });

        return attachmentDtos;
    }
}
