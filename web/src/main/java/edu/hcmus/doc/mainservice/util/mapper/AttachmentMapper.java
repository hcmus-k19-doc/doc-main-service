package edu.hcmus.doc.mainservice.util.mapper;

import edu.hcmus.doc.mainservice.model.dto.AttachmentDto;
import edu.hcmus.doc.mainservice.model.entity.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "alfrescoFileId", target = "alfrescoFileId")
  @Mapping(source = "alfrescoFolderId", target = "alfrescoFolderId")
  @Mapping(source = "fileType", target = "fileType")
  AttachmentDto toDto(Attachment attachment);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "alfrescoFileId", target = "alfrescoFileId")
  @Mapping(source = "alfrescoFolderId", target = "alfrescoFolderId")
  @Mapping(source = "fileType", target = "fileType")
  Attachment toEntity(AttachmentDto attachmentDto);
}
