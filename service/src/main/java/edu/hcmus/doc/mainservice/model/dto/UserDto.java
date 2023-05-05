package edu.hcmus.doc.mainservice.model.dto;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import javax.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDto extends DocAbstractDto {

  private String username;
  @Email(message = "user.email.invalid")
  private String email;
  private String fullName;
  private String password;
  private DocSystemRoleEnum role;
  private DepartmentDto department;
}
