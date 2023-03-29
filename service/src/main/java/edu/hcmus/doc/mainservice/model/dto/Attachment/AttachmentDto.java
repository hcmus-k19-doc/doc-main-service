package edu.hcmus.doc.mainservice.model.dto.Attachment;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AttachmentDto extends DocAbstractDto {

  private String alfrescoFileId;
  private String alfrescoFolderId;
  private String fileType;
}
