package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/attachments")
public class AttachmentController {

  private final AttachmentService attachmentService;

  @PostMapping("/upload")
  public ResponseEntity<?> uploadAttachments(@ModelAttribute IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto) {
    return ResponseEntity.ok("Upload success");
  }
}
