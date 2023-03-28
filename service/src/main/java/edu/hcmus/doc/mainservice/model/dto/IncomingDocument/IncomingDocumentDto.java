package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.dto.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.DistributionOrganizationDto;
import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.dto.FolderDto;
import edu.hcmus.doc.mainservice.model.dto.SendingLevelDto;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private LocalDate arrivingDate;
    private String summary;
    private SendingLevelDto sendingLevel;
    private FolderDto folder;
    private List<AttachmentDto> attachments = new ArrayList<>();
}