package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;

public interface CustomOutgoingDocumentRepository {
  OutgoingDocument getOutgoingDocumentById(Long id);
}
