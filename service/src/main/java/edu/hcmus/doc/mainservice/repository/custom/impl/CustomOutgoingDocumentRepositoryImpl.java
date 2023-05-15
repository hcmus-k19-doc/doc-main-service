package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.entity.*;
import edu.hcmus.doc.mainservice.repository.custom.CustomOutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;

public class CustomOutgoingDocumentRepositoryImpl
    extends DocAbstractCustomRepository<OutgoingDocument>
    implements CustomOutgoingDocumentRepository {

    @Override
    public OutgoingDocument getOutgoingDocumentById(Long id) {
        return selectFrom(QOutgoingDocument.outgoingDocument)
                .join(QOutgoingDocument.outgoingDocument.folder, QFolder.folder)
                .fetchJoin()
                .join(QOutgoingDocument.outgoingDocument.documentType, QDocumentType.documentType)
                .fetchJoin()
                .join(QOutgoingDocument.outgoingDocument.publishingDepartment, QDepartment.department)
                .fetchJoin()
                .where(QOutgoingDocument.outgoingDocument.id.eq(id))
                .fetchFirst();
    }
}
