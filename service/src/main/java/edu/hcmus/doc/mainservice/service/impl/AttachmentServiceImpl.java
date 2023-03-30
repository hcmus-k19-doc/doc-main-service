package edu.hcmus.doc.mainservice.service.impl;


import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.FileDto;
import edu.hcmus.doc.mainservice.repository.AttachmentRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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

  private final AttachmentRepository attachmentRepository;

  private final AsyncRabbitTemplate asyncRabbitTemplate;

  private final AttachmentMapperDecorator attachmentMapperDecorator;

  @SneakyThrows
  @Override
  public List<AttachmentDto> saveAttachmentsByIncomingDocId(AttachmentPostDto attachmentPostDto) {
    RabbitConverterFuture<List<FileDto>> rabbitConverterFuture = asyncRabbitTemplate
        .convertSendAndReceiveAsType(exchange, routingkey, attachmentPostDto,
            new ParameterizedTypeReference<>() {
            });

    rabbitConverterFuture.addCallback(
        new ListenableFutureCallback<>() {
          @Override
          public void onFailure(Throwable ex) {
//            throw new FileServiceFailureException(FileServiceFailureException.FILE_SERVICE_FAILURE);
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
        .map(fileDto -> attachmentMapperDecorator.convertFileDtoToAttachmentDto(attachmentPostDto.getIncomingDocId(), fileDto)).toList();

    attachmentDtos.stream().map(attachmentMapperDecorator::toEntity).forEach(attachmentRepository::save);

    return attachmentDtos;
  }
}
