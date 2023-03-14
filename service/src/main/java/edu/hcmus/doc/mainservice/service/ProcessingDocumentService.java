package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;

public interface ProcessingDocumentService {

  long getTotalElements(String query);

  long getTotalPages(String query, long limit);

  List<ProcessingDocument> getIncomingDocuments(String query, long offset, long limit);
}
