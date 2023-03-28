package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.AttachmentDto;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

  @Override
  public List<AttachmentDto> saveAttachmentsByIncomingDocId(List<AttachmentDto> attachmentDtos,
      Long incomingDocId) {
    return null;
  }
}
