package edu.hcmus.doc.mainservice.model.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SearchCriteriaDto {

  private String incomingNumber;
  private String originalSymbolNumber;
  private String documentType;
  private String distributionOrg;
  private LocalDate arrivingDate;
  private LocalDate processingDuration;
  private String summary;
}
