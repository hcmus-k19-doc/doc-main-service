package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.service.FolderService;
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

  private final FolderService folderService;

  private final ProcessingDocumentRepository processingDocumentRepository;

  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return processingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    return getTotalElements(searchCriteriaDto) / limit;
  }

  @Override
  public List<ProcessingDocument> searchIncomingDocuments(SearchCriteriaDto searchCriteria, int page, int pageSize) {
    return processingDocumentRepository.searchByCriteria(searchCriteria, page, pageSize);
  }

  @Override
  public List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit) {
    return incomingDocumentRepository.getIncomingDocuments(query, offset, limit);
  }

  @Override
  public IncomingDocument createIncomingDocument(IncomingDocument incomingDocument) {
    Folder folder = folderService.findById(incomingDocument.getFolder().getId());
    folder.setNextNumber(folder.getNextNumber() + 1);

    return incomingDocumentRepository.save(incomingDocument);
  }
}
