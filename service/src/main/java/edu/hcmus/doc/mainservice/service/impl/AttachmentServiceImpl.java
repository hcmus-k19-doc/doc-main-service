package edu.hcmus.doc.mainservice.service.impl;


import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.FileDto;
import edu.hcmus.doc.mainservice.model.exception.FileServiceFailureException;
import edu.hcmus.doc.mainservice.repository.AttachmentRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class AttachmentServiceImpl implements AttachmentService {

  private final AttachmentRepository attachmentRepository;

  private final AsyncRabbitTemplate asyncRabbitTemplate;

  @Value("${spring.rabbitmq.template.exchange}")
  private String exchange;

  @Value("${spring.rabbitmq.template.routing-key}")
  private String routingkey;

  @SneakyThrows
  @Override
  public List<AttachmentDto> saveAttachmentsByIncomingDocId(AttachmentPostDto attachmentPostDto) {

//    // call api to doc-file-service to save files
//    byte[] requestBytes = SerializationUtils.serialize(attachmentPostDto);
//
//    MessageProperties messageProperties = new MessageProperties();
//    messageProperties.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//
//    Message requestMessage = new Message(requestBytes);

    RabbitConverterFuture<List<FileDto>> rabbitConverterFuture = asyncRabbitTemplate
        .convertSendAndReceiveAsType(exchange, routingkey, attachmentPostDto,
            new ParameterizedTypeReference<>() {
            });

    rabbitConverterFuture.addCallback(
        result -> {
          // save file info to db
          log.info("result: " + result);
        },
        ex -> {
          // handle failure
          log.error("Error: " + ex.getMessage());
          throw new FileServiceFailureException(FileServiceFailureException.FILE_SERVICE_FAILURE);
        }
    );
    // save file info to db
    List<FileDto> fileDtos = rabbitConverterFuture.get();
    System.out.println("fileDtos: " + fileDtos);
    return null;
  }
}
