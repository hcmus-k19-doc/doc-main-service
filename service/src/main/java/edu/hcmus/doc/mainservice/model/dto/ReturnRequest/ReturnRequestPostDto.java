package edu.hcmus.doc.mainservice.model.dto.ReturnRequest;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import lombok.Data;

@Data
public class ReturnRequestPostDto {
  private Long currentProcessingUserId;
  private Long previousProcessingUserId;
  private Long documentId;
  private ProcessingDocumentTypeEnum type;
  private String reason;
}
