package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.IncomingDocument} entity
 */
@Data
public class IncomingDocumentDto extends DocAbstractDto {
    private String incomingNumber;
    private Long documentTypeId;
    private String originalSymbolNumber;
    private Long distributionOrgId;
    private LocalDate distributionDate;
    private LocalDate arrivingDate;
    private LocalTime arrivingTime;
    private String summary;
    private Urgency urgency;
    private Confidentiality confidentiality;
    private String folder;
    private Long sendingLevelId;
}