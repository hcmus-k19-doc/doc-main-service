package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import lombok.Data;

@Data
public class ProcessingUserDto {

  private Long id;
  private String fullName;
  private ProcessingDocumentRoleEnum role;
  private String department;
}