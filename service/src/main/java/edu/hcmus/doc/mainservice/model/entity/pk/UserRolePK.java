package edu.hcmus.doc.mainservice.model.entity.pk;

import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserRolePK implements Serializable {

  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "role_name", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
  private DocSystemRoleEnum role;
}
