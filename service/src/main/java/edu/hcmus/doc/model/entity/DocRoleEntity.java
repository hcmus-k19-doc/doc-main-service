package edu.hcmus.doc.model.entity;

import edu.hcmus.doc.model.enums.DocRole;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "doc_role", schema = "public")
public class DocRoleEntity extends DocAbstractEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(10)")
  private DocRole name;
}
