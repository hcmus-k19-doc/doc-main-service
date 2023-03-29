package edu.hcmus.doc.mainservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.Attachment.FileWrapper;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.util.mapper.IncomingDocumentMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class IncomingDocumentServiceImpl implements IncomingDocumentService {

  private final IncomingDocumentRepository incomingDocumentRepository;
  private final FolderService folderService;
  private final ObjectMapper objectMapper;
  private final IncomingDocumentMapper incomingDecoratorDocumentMapper;
  private final AttachmentService attachmentService;

  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return incomingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @SneakyThrows
  @Override
  public IncomingDocument createIncomingDocument(IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto) {
    IncomingDocumentPostDto incomingDocumentPostDto = objectMapper.readValue(
        incomingDocumentWithAttachmentPostDto.getIncomingDocumentPostDto(), IncomingDocumentPostDto.class);
    IncomingDocument incomingDocument = incomingDecoratorDocumentMapper.toEntity(
        incomingDocumentPostDto);

    Folder folder = folderService.findById(incomingDocument.getFolder().getId());
    folder.setNextNumber(folder.getNextNumber() + 1);
    IncomingDocument savedIncomingDocument = incomingDocumentRepository.save(incomingDocument);

    AttachmentPostDto attachmentPostDto = new AttachmentPostDto();
    attachmentPostDto.setIncomingDocId(savedIncomingDocument.getId());
    List<FileWrapper> fileWrappers = incomingDocumentWithAttachmentPostDto.getAttachments().stream()
        .map(file -> {
          FileWrapper fileWrapper = new FileWrapper();
          try {
            fileWrapper.setData(file.getBytes());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          fileWrapper.setFileName(file.getOriginalFilename());
          fileWrapper.setContentType(file.getContentType());
          return fileWrapper;
        }).collect(Collectors.toList());
    attachmentPostDto.setAttachments(fileWrappers);
    List<AttachmentDto> attachmentDtos = attachmentService.saveAttachmentsByIncomingDocId(
        attachmentPostDto);
    return savedIncomingDocument;
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    return getTotalElements(searchCriteriaDto) / limit;
  }

  @Override
  public List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit) {
    return incomingDocumentRepository.getIncomingDocuments(query, offset, limit);
  }
}
