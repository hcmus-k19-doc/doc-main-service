package edu.hcmus.doc.mainservice.model.dto.ReturnRequest;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentTypeEnum;
import java.util.List;
import lombok.Data;

@Data
public class ReturnRequestPostDto {
  private Long currentProcessingUserId;
  private Long previousProcessingUserId;
  private List<Long> documentIds;
  private ProcessingDocumentTypeEnum type;
  private String reason;
  private Integer step;
}
