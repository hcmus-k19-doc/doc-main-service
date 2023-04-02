package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;

public interface IncomingDocumentService {

  long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);

  List<ProcessingDocument> searchIncomingDocuments(SearchCriteriaDto searchCriteria, int page, int pageSize);

  @Deprecated(since = "1.0.0", forRemoval = true)
  List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit);

  IncomingDocument createIncomingDocument(IncomingDocument incomingDocument);
}
