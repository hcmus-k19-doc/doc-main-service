package edu.hcmus.doc.mainservice.model.dto.Attachment;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentPostDto {
  private String incomingDocumentId;
  private List<MultipartFile> attachments;
}
