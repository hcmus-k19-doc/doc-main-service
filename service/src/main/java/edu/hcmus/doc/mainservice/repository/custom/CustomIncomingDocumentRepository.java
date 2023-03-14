package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import java.util.List;

public interface CustomIncomingDocumentRepository {

  Long getTotalElements(String query);

  List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit);
}
