package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class ProcessingDocumentServiceImpl implements ProcessingDocumentService {

  private final ProcessingDocumentRepository processingDocumentRepository;

  @Override
  public long getTotalElements(String query) {
    return processingDocumentRepository.getTotalElements(query);
  }

  @Override
  public long getTotalPages(String query, long limit) {
    return getTotalElements(query) / limit;
  }

  @Override
  public List<ProcessingDocument> getIncomingDocuments(String query, long offset, long limit) {
    return processingDocumentRepository.getProcessingDocuments(query, offset, limit);
  }
}
