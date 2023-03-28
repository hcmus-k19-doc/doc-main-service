package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentDto {

  protected Long id;
  protected Long version;
  private String alfrescoFileId;
  private String alfrescoFolderId;
  private String fileType;
  private MultipartFile data;
}
