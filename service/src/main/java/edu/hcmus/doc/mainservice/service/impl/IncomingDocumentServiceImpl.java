package edu.hcmus.doc.mainservice.service.impl;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.FolderNotFoundException;
import edu.hcmus.doc.mainservice.repository.FolderRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.util.DocObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class IncomingDocumentServiceImpl implements IncomingDocumentService {

    private final IncomingDocumentRepository incomingDocumentRepository;
    private final FolderRepository folderRepository;

    @Override
    public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
        return incomingDocumentRepository.getTotalElements(searchCriteriaDto);
    }

    @Override
    public IncomingDocument updateIncomingDocument(IncomingDocument incomingDocument) {
        IncomingDocument updatingIncomingDocument = getIncomingDocumentById(incomingDocument.getId());
        DocObjectUtils.copyNonNullProperties(incomingDocument, updatingIncomingDocument);
        return incomingDocumentRepository.saveAndFlush(updatingIncomingDocument);
    }

    @Override
    public IncomingDocument createIncomingDocument(IncomingDocument incomingDocument) {
        Folder folder = folderRepository.findById(incomingDocument.getFolder().getId())
                .orElseThrow(() -> new FolderNotFoundException(FolderNotFoundException.FOLDER_NOT_FOUND));
        folder.setNextNumber(folder.getNextNumber() + 1);

        return incomingDocumentRepository.save(incomingDocument);
    }

    @Override
    public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
        return getTotalElements(searchCriteriaDto) / limit;
    }

    @Override
    public List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit) {
        return incomingDocumentRepository.getIncomingDocuments(query, offset, limit);
    }

    @Override
    public IncomingDocument getIncomingDocumentById(Long id) {
        IncomingDocument incomingDocument = incomingDocumentRepository.getIncomingDocumentById(id);

        if (ObjectUtils.isEmpty(incomingDocument)) {
            throw new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND);
        }

        return incomingDocument;
    }
}
