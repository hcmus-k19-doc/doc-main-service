package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.AttachmentDto;
import edu.hcmus.doc.mainservice.repository.AttachmentRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class AttachmentServiceImpl implements AttachmentService {

  private final AttachmentRepository attachmentRepository;

  private final AsyncRabbitTemplate asyncRabbitTemplate;

  @Value("${spring.rabbitmq.template.exchange}")
  private String exchange;

  @Value("${spring.rabbitmq.template.routing-key}")
  private String routingkey;

  @Override
  public List<AttachmentDto> saveAttachmentsByIncomingDocId(List<MultipartFile> attachments,
      Long incomingDocId) {
    // call api to doc-file-service to save files

    // save file info to db
    return null;
  }
}
