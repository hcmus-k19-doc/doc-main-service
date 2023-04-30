package edu.hcmus.doc.mainservice.model.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.Hibernate;

@Data
@MappedSuperclass
public abstract class DocAbstractIdEntity extends DocAbstractVersionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, columnDefinition = "SERIAL")
  @NotNull
  protected Long id;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    DocAbstractIdEntity that = (DocAbstractIdEntity) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
