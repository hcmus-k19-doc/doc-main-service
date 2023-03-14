package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;

public interface CustomProcessingDocumentRepository {

  Long getTotalElements(String query);

  List<ProcessingDocument> getProcessingDocuments(String query, long offset, long limit);
}
