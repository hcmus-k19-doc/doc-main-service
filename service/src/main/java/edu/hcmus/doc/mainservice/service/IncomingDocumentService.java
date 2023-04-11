package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import java.util.List;

public interface IncomingDocumentService {

  long getTotalElements(SearchCriteriaDto searchCriteriaDto);

  IncomingDocument createIncomingDocument(IncomingDocument incomingDocument);

  IncomingDocument updateIncomingDocument(IncomingDocument incomingDocument);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);

  List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit);

  IncomingDocument getIncomingDocumentById(Long id);
}
