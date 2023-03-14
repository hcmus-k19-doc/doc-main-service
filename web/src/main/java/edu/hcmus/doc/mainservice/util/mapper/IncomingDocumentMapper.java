package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(IncomingDocumentMapperDecorator.class)
public interface IncomingDocumentMapper {

  IncomingDocumentDto toDto(IncomingDocument incomingDocument);

  IncomingDocumentDto toDto(ProcessingDocument processingDocument);
}
