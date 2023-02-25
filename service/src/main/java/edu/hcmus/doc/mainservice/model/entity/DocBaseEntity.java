package edu.hcmus.doc.mainservice.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public abstract class DocBaseEntity {

  @Version
  @Column(name = "version", nullable = false, columnDefinition = "INT DEFAULT 0")
  protected Long version;

  @CreationTimestamp
  @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  protected LocalDateTime createdDate;

  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false, columnDefinition = "BIGINT")
  protected Long createdBy;

  @UpdateTimestamp
  @Column(name = "updated_date", nullable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()")
  protected LocalDateTime updatedDate;

  @Column(name = "updated_by", nullable = false, columnDefinition = "BIGINT")
  protected Long updatedBy;
}
