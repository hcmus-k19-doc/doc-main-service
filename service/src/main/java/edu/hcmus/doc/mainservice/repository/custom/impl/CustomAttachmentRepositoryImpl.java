package edu.hcmus.doc.mainservice.repository.custom.impl;

import com.querydsl.core.BooleanBuilder;
import edu.hcmus.doc.mainservice.model.entity.Attachment;
import edu.hcmus.doc.mainservice.model.entity.QAttachment;
import edu.hcmus.doc.mainservice.model.enums.ParentFolderEnum;
import edu.hcmus.doc.mainservice.repository.custom.CustomAttachmentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomAttachmentRepositoryImpl
    extends DocAbstractCustomRepository<Attachment>
    implements CustomAttachmentRepository {

  @Override
  public List<Attachment> getAttachmentsDocId(Long docId, ParentFolderEnum parentFolder) {
    BooleanBuilder where = new BooleanBuilder();
    if (parentFolder == ParentFolderEnum.ICD) {
      where.and(QAttachment.attachment.incomingDoc.id.eq(docId));
    } else {
      where.and(QAttachment.attachment.outgoingDocument.id.eq(docId));
    }
    return select(QAttachment.attachment.id,
        QAttachment.attachment.alfrescoFileId,
        QAttachment.attachment.alfrescoFolderId,
        QAttachment.attachment.fileType)
        .from(QAttachment.attachment)
        .where(where)
        .fetch()
        .stream()
        .map(tuple -> {
          Attachment attachment = new Attachment();
          attachment.setId(tuple.get(QAttachment.attachment.id));
          attachment.setAlfrescoFileId(tuple.get(QAttachment.attachment.alfrescoFileId));
          attachment.setAlfrescoFolderId(tuple.get(QAttachment.attachment.alfrescoFolderId));
          attachment.setFileType(tuple.get(QAttachment.attachment.fileType));
          return attachment;
        })
        .toList();
  }
}
