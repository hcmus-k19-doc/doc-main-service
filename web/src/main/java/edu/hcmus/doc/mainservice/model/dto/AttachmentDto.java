package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

@Data
public class AttachmentDto extends DocAbstractDto{
  private String alfrescoFileId;
  private String alfrescoFolderId;
  private String fileType;
}
