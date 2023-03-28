package edu.hcmus.doc.mainservice.model.dto.IncomingDocument;

import edu.hcmus.doc.mainservice.model.dto.AttachmentDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.web.multipart.MultipartFile;

@Data
public class IncomingDocumentPostDto {
    private String incomingNumber;
    private Long documentType;
    private String originalSymbolNumber;
    private Long distributionOrg;
    private LocalDateTime distributionDate;
    private LocalDate arrivingDate;
    private LocalTime arrivingTime;
    private String summary;
    private Urgency urgency;
    private Confidentiality confidentiality;
    private Long folder;
    private MultipartFile[] attachments;
}
