package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.AttachmentDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

  public List<AttachmentDto> saveAttachmentsByIncomingDocId(List<MultipartFile> attachments,
      Long incomingDocId);
}
