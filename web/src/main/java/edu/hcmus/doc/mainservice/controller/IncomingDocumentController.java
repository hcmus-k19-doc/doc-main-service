package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/incoming-documents")
public class IncomingDocumentController extends DocAbstractController {

    private final ProcessingDocumentService processingDocumentService;
    private final IncomingDocumentService incomingDocumentService;

    @GetMapping("/{id}")
    public IncomingDocumentDto getIncomingDocument(@PathVariable Long id) {
        return incomingDecoratorDocumentMapper.toDto(incomingDocumentService.getIncomingDocumentById(id));
    }

    @PostMapping("/search")
    public DocPaginationDto<IncomingDocumentDto> getIncomingDocuments(@RequestBody(required = false) SearchCriteriaDto searchCriteria, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "3") int pageSize) {

        return paginationMapper.toDto(processingDocumentService.searchProcessingDocuments(searchCriteria, page, pageSize).stream().map(incomingDecoratorDocumentMapper::toDto).toList(), processingDocumentService.getTotalElements(searchCriteria), processingDocumentService.getTotalPages(searchCriteria, pageSize));
    }

    @PostMapping("/create")
    public IncomingDocumentDto createIncomingDocument(@RequestBody IncomingDocumentPostDto incomingDocumentPostDto) {
        IncomingDocument incomingDocument = incomingDecoratorDocumentMapper.toEntity(incomingDocumentPostDto);
        return incomingDecoratorDocumentMapper.toDto(incomingDocumentService.createIncomingDocument(incomingDocument));
    }

    @PutMapping("/update")
    public IncomingDocumentDto createIncomingDocument(@RequestBody IncomingDocumentPutDto incomingDocumentPutDto) {
        IncomingDocument incomingDocument = incomingDecoratorDocumentMapper.toEntity(incomingDocumentPutDto);
        return incomingDecoratorDocumentMapper.toDto(incomingDocumentService.updateIncomingDocument(incomingDocument));
    }
}
