package edu.hcmus.doc.model.entity.pk;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserRolePK implements Serializable {

  private Long userId;
  private Long roleId;
}
