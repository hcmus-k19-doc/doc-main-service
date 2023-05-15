package edu.hcmus.doc.mainservice.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;

@Data
public class OutgoingDocSearchCriteriaDto {

  private String outgoingNumber;
  private Long documentTypeId;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate releaseDateFrom;
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate releaseDateTo;
  private String summary;
}
