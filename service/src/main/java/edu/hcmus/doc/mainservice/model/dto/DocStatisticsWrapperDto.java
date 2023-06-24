package edu.hcmus.doc.mainservice.model.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DocStatisticsWrapperDto {
  private List<DocStatisticsDto> docStatistics;
  private LocalDate fromDate;
  private LocalDate toDate;
}
