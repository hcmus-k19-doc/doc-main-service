package edu.hcmus.doc.mainservice.model.entity;

import static edu.hcmus.doc.mainservice.DocConst.CATALOG_NAME;
import static edu.hcmus.doc.mainservice.DocConst.SCHEMA_NAME;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transfer_history_incoming_document", schema = SCHEMA_NAME, catalog = CATALOG_NAME)
public class TransferHistoryAndIncomingDoc extends DocAbstractIdEntity {

  @Column(name = "transfer_history_id", columnDefinition = "BIGINT", nullable = false)
  private Long transferHistoryId;


  @Column(name = "incoming_doc_id", columnDefinition = "BIGINT", nullable = false)
  private Long incomingDocId;
}
