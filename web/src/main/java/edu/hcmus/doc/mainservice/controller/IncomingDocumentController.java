package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/incoming-documents")
public class IncomingDocumentController extends DocAbstractController {

  private final ProcessingDocumentService processingDocumentService;
  private final IncomingDocumentService incomingDocumentService;
  private final AttachmentService attachmentService;


    @PostMapping("/search")
    public DocPaginationDto<IncomingDocumentDto> getIncomingDocuments(
            @RequestBody(required = false) SearchCriteriaDto searchCriteria,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "3") int pageSize
    ) {

        return paginationMapper.toDto(
                processingDocumentService
                        .searchProcessingDocuments(searchCriteria, page, pageSize)
                        .stream()
                        .map(incomingDecoratorDocumentMapper::toDto)
                        .toList(),
                processingDocumentService.getTotalElements(searchCriteria),
                processingDocumentService.getTotalPages(searchCriteria, pageSize));
    }

  @PostMapping("/create")
  public IncomingDocumentDto createIncomingDocument(
      @RequestBody IncomingDocumentPostDto incomingDocumentPostDto) {
    IncomingDocument incomingDocument = incomingDocumentService.createIncomingDocument(
        incomingDecoratorDocumentMapper.toEntity(incomingDocumentPostDto));
//    attachmentService.saveAttachmentsByIncomingDocId(incomingDocumentPostDto.getAttachmentDtos(),
//        incomingDocument.getId());

    IncomingDocumentDto incomingDocumentDto = incomingDecoratorDocumentMapper.toDto(
        incomingDocument);
    return incomingDocumentDto;
  }
}
