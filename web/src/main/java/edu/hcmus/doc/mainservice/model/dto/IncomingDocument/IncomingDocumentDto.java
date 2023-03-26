package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.dto.*;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import edu.hcmus.doc.mainservice.model.enums.Urgency;
import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.IncomingDocument} entity
 */
@Data
public class IncomingDocumentDto extends DocAbstractDto {
    private ProcessingStatus status;
    private LocalDate processingDuration;
    private String incomingNumber;
    private DocumentTypeDto documentType;
    private String originalSymbolNumber;
    private DistributionOrganizationDto distributionOrg;
    private LocalDate distributionDate;
    private LocalDate arrivingDate;
    private LocalTime arrivingTime;
    private String summary;
    private SendingLevelDto sendingLevel;
    private FolderDto folder;
    private Urgency urgency;
    private Confidentiality confidentiality;
}