package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import java.util.List;

public interface IncomingDocumentService {

  long getTotalElements(String query);

  long getTotalPages(String query, long limit);

  List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit);
}
