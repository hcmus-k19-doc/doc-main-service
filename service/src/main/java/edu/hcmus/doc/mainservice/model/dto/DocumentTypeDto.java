package edu.hcmus.doc.mainservice.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A DTO for the {@link edu.hcmus.doc.mainservice.model.entity.DocumentType} entity
 */
@Data
public class DocumentTypeDto extends DocAbstractDto {

  @NotBlank
  private String type;
}
