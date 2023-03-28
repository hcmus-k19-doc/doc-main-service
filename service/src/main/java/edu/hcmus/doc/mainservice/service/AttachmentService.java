package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.AttachmentDto;
import java.util.List;

public interface AttachmentService {

  public List<AttachmentDto> saveAttachmentsByIncomingDocId(List<AttachmentDto> attachmentDtos,
      Long incomingDocId);
}
