package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

  public List<AttachmentDto> saveAttachmentsByIncomingDocId(AttachmentPostDto attachmentPostDto);
}
