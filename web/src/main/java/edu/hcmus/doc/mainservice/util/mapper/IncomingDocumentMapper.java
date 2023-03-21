package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(IncomingDocumentMapperDecorator.class)
public interface IncomingDocumentMapper {

    @Mapping(source = "incomingNumber", target = "incomingNumber")
    @Mapping(source = "originalSymbolNumber", target = "originalSymbolNumber")
    @Mapping(source = "distributionOrg", target = "distributionOrgId")
    @Mapping(source = "distributionDate", target = "distributionDate")
    @Mapping(source = "documentType", target = "documentTypeId")
    @Mapping(source = "arrivingDate", target = "arrivingDate")
    @Mapping(source = "arrivingTime", target = "arrivingTime")
    @Mapping(source = "summary", target = "summary")
    @Mapping(source = "urgency", target = "urgency")
    @Mapping(source = "confidentiality", target = "confidentiality")
    @Mapping(source = "folder", target = "folder")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "processingDuration", ignore = true)
    IncomingDocumentDto toDto(IncomingDocument incomingDocument);

    @Mapping(source = "incomingNumber", target = "incomingNumber")
    @Mapping(source = "originalSymbolNumber", target = "originalSymbolNumber")
    @Mapping(source = "distributionOrgId", target = "distributionOrg")
    @Mapping(source = "distributionDate", target = "distributionDate")
    @Mapping(source = "documentTypeId", target = "documentType")
    @Mapping(source = "arrivingDate", target = "arrivingDate")
    @Mapping(source = "arrivingTime", target = "arrivingTime")
    @Mapping(source = "summary", target = "summary")
    @Mapping(source = "urgency", target = "urgency")
    @Mapping(source = "sendingLevelId", target = "sendingLevel")
    @Mapping(source = "confidentiality", target = "confidentiality")
    @Mapping(source = "folder", target = "folder")
    IncomingDocument toEntity(IncomingDocumentDto dto);

    @Mapping(target = "summary", ignore = true)
    @Mapping(target = "sendingLevel", ignore = true)
    @Mapping(target = "originalSymbolNumber", ignore = true)
    @Mapping(target = "incomingNumber", ignore = true)
    @Mapping(target = "documentType", ignore = true)
    @Mapping(target = "distributionOrg", ignore = true)
    @Mapping(target = "arrivingDate", ignore = true)
    IncomingDocumentDto toDto(ProcessingDocument processingDocument);
}
