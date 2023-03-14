package edu.hcmus.doc.mainservice.model.dto;

import java.time.LocalDate;
import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.IncomingDocument} entity
 */
@Data
public class IncomingDocumentDto extends DocAbstractDto {

  private String incomingNumber;
  private DocumentTypeDto documentType;
  private String originalSymbolNumber;
  private DistributionOrganizationDto distributionOrg;
  private LocalDate arrivingDate;
  private String summary;
  private SendingLevelDto sendingLevel;
}