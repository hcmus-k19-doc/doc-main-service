package edu.hcmus.doc.mainservice.model.dto.OutgoingDocument;

import edu.hcmus.doc.mainservice.model.dto.DocAbstractDto;
import edu.hcmus.doc.mainservice.model.enums.Confidentiality;
import edu.hcmus.doc.mainservice.model.enums.Urgency;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OutgoingDocumentPutDto extends DocAbstractDto {
    @NotBlank
    private String name;
    private String outgoingNumber;
    @NotBlank
    private String originalSymbolNumber;
    @NotBlank
    private String recipient;
    private String signer;
    @NotBlank
    private String summary;
    @NotNull
    private Urgency urgency;
    @NotNull
    @FutureOrPresent
    private LocalDate releaseDate;
    @NotNull
    private Confidentiality confidentiality;
    @NotNull
    private Long documentType;
    @NotNull
    private Long folder;
    @NotNull
    private Long publishingDepartment;
}
