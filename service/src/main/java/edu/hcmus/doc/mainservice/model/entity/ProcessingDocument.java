package edu.hcmus.doc.mainservice.model.entity;

import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "processing_document", schema = "doc_main", catalog = "doc")
public class ProcessingDocument extends DocAbstractEntity {

  @OneToOne
  @JoinColumn(name = "incoming_doc_id", referencedColumnName = "id", nullable = false)
  private IncomingDocument incomingDoc;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ProcessingStatus status;

  @Column(name = "is_opened", nullable = false)
  private boolean isOpened;

  @Column(name = "processing_duration")
  private LocalTime processingDuration;

  @Column(name = "processing_request", nullable = false)
  private String processingRequest;
}
