package edu.hcmus.doc.mainservice.model.dto;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StatisticsDto {

  private Integer numberOfUnprocessedDocument;
  private Integer numberOfProcessingDocument;
  private Integer numberOfProcessedDocument;
  private List<String> data;
}
