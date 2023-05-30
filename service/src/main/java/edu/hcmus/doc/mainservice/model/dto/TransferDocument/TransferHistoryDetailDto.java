package edu.hcmus.doc.mainservice.model.dto.TransferDocument;

import edu.hcmus.doc.mainservice.model.enums.ProcessMethod;
import java.time.LocalDate;

public class TransferHistoryDetailDto {

  private Long documentId;
  private LocalDate createdDate;
  private LocalDate processingDuration;
  private Boolean isInfiniteProcessingTime = false;
  private ProcessMethod processMethod;
  private Long senderId;
  private String senderName;
  private Long receiverId;
  private String receiverName;
}
