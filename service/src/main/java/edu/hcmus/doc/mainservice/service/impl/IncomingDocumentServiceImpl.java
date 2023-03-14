package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class IncomingDocumentServiceImpl implements IncomingDocumentService {

  private final IncomingDocumentRepository incomingDocumentRepository;

  @Override
  public long getTotalElements(String query) {
    return incomingDocumentRepository.getTotalElements(query);
  }

  @Override
  public long getTotalPages(String query, long limit) {
    return getTotalElements(query) / limit;
  }

  @Override
  public List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit) {
    return incomingDocumentRepository.getIncomingDocuments(query, offset, limit);
  }
}
