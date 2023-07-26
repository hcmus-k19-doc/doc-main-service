package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.exception.DocMainServiceRuntimeException;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/attachments")
public class AttachementController extends DocAbstractController {
    private final AttachmentService attachmentService;

    @DeleteMapping("/{id}")
    public void deleteAttachmentById(@PathVariable Long id) {
        if(Objects.isNull(attachmentService.deleteAttachmentById(id))){
            throw new DocMainServiceRuntimeException("Attachment not found");
        }
    }
}
