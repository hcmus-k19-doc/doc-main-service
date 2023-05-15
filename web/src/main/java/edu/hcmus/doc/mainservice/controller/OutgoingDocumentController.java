package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentGetDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocument.OutgoingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.service.OutgoingDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/outgoing-documents")
public class OutgoingDocumentController extends DocAbstractController {
  private final OutgoingDocumentService outgoingDocumentService;

  @GetMapping("/{id}")
  public OutgoingDocumentGetDto getOutgoingDocument(@PathVariable Long id) {
    return outgoingDecoratorDocumentMapper
            .toDto(outgoingDocumentService.getOutgoingDocumentById(id));
  }

  @PutMapping("/update")
  public OutgoingDocumentGetDto updateOutgoingDocument(
          @RequestBody OutgoingDocumentPutDto outgoingDocumentPutDto) {
    OutgoingDocument outgoingDocument = outgoingDecoratorDocumentMapper.toEntity(
            outgoingDocumentPutDto);
    return outgoingDecoratorDocumentMapper.toDto(
            outgoingDocumentService.updateOutgoingDocument(outgoingDocument));
  }

  @SneakyThrows
  @PostMapping("/create")
  public OutgoingDocumentGetDto createIncomingDocument(
          @ModelAttribute OutgoingDocumentWithAttachmentPostDto outgoingDocumentWithAttachmentPostDto) {
    return outgoingDecoratorDocumentMapper.toDto(
            outgoingDocumentService.createOutgoingDocument(outgoingDocumentWithAttachmentPostDto));
  }

  @PostMapping("/search")
  public DocPaginationDto<OutgoingDocumentGetDto> getOutgoingDocuments(
      @RequestBody(required = false) OutgoingDocSearchCriteriaDto searchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "3") int pageSize
  ) {
    return paginationMapper.toDto(
        outgoingDocumentService
            .searchOutgoingDocuments(searchCriteria, page, pageSize)
            .stream()
            .map(outgoingDecoratorDocumentMapper::toDto)
            .toList(),
        outgoingDocumentService.getTotalElements(searchCriteria),
        outgoingDocumentService.getTotalPages(searchCriteria, pageSize));
  }
}
