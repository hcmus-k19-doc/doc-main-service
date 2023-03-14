package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncomingDocumentMapper {

  IncomingDocumentDto toDto(IncomingDocument incomingDocument);
}
