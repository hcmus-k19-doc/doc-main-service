package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/incoming-documents")
public class IncomingDocumentController extends DocAbstractController {

  private final IncomingDocumentService incomingDocumentService;

  @GetMapping
  public DocPaginationDto<IncomingDocumentDto> getIncomingDocuments(
      @RequestParam(required = false, defaultValue = "") String query,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "3") int pageSize
  ) {

    return paginationMapper.toDto(
        incomingDocumentService
            .getIncomingDocuments(query, page, pageSize)
            .stream()
            .map(incomingDocumentMapper::toDto)
            .toList(),
        incomingDocumentService.getTotalElements(query),
        incomingDocumentService.getTotalPages(query, pageSize));
  }
}
