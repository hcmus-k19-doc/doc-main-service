package edu.hcmus.doc.mainservice.repository.custom.impl;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QDistributionOrganization;
import edu.hcmus.doc.mainservice.model.entity.QDocumentType;
import edu.hcmus.doc.mainservice.model.entity.QIncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.QProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.QSendingLevel;
import edu.hcmus.doc.mainservice.repository.custom.CustomProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;

public class CustomProcessingDocumentRepositoryImpl extends
    DocAbstractCustomRepository<ProcessingDocument> implements CustomProcessingDocumentRepository {

  @Override
  public Long getTotalElements(String query) {
    return selectFrom(QProcessingDocument.processingDocument).select(
        QProcessingDocument.processingDocument.id.count()).fetchOne();
  }

  @Override
  public List<ProcessingDocument> getProcessingDocuments(String query, long offset, long limit) {
    return select(
        QProcessingDocument.processingDocument.id,
        QProcessingDocument.processingDocument.status,
        QProcessingDocument.processingDocument.processingDuration,
        QProcessingDocument.processingDocument.incomingDoc.id,
        QProcessingDocument.processingDocument.incomingDoc.incomingNumber,
        QProcessingDocument.processingDocument.incomingDoc.originalSymbolNumber,
        QProcessingDocument.processingDocument.incomingDoc.arrivingDate,
        QProcessingDocument.processingDocument.incomingDoc.summary,
        QProcessingDocument.processingDocument.incomingDoc.sendingLevel.id,
        QProcessingDocument.processingDocument.incomingDoc.sendingLevel.level,
        QProcessingDocument.processingDocument.incomingDoc.documentType.id,
        QProcessingDocument.processingDocument.incomingDoc.documentType.type,
        QProcessingDocument.processingDocument.incomingDoc.distributionOrg.id,
        QProcessingDocument.processingDocument.incomingDoc.distributionOrg.name)
        .from(QProcessingDocument.processingDocument)
        .leftJoin(QProcessingDocument.processingDocument.incomingDoc, QIncomingDocument.incomingDocument)
        .innerJoin(QIncomingDocument.incomingDocument.sendingLevel, QSendingLevel.sendingLevel)
        .innerJoin(QIncomingDocument.incomingDocument.documentType, QDocumentType.documentType)
        .innerJoin(QIncomingDocument.incomingDocument.distributionOrg, QDistributionOrganization.distributionOrganization)
        .orderBy(QIncomingDocument.incomingDocument.id.asc())
        .offset(offset * limit)
        .limit(limit)
        .fetch()
        .stream()
        .map(tuple -> {
          ProcessingDocument processingDocument = new ProcessingDocument();
          processingDocument.setId(tuple.get(QProcessingDocument.processingDocument.id));
          processingDocument.setStatus(tuple.get(QProcessingDocument.processingDocument.status));
          processingDocument.setProcessingDuration(tuple.get(QProcessingDocument.processingDocument.processingDuration));
          processingDocument.getIncomingDoc().setId(tuple.get(QProcessingDocument.processingDocument.incomingDoc.id));
          processingDocument.getIncomingDoc().setIncomingNumber(tuple.get(QProcessingDocument.processingDocument.incomingDoc.incomingNumber));
          processingDocument.getIncomingDoc().setOriginalSymbolNumber(tuple.get(QProcessingDocument.processingDocument.incomingDoc.originalSymbolNumber));
          processingDocument.getIncomingDoc().setArrivingDate(tuple.get(QProcessingDocument.processingDocument.incomingDoc.arrivingDate));
          processingDocument.getIncomingDoc().setSummary(tuple.get(QProcessingDocument.processingDocument.incomingDoc.summary));
          processingDocument.getIncomingDoc().getSendingLevel().setId(tuple.get(QProcessingDocument.processingDocument.incomingDoc.sendingLevel.id));
          processingDocument.getIncomingDoc().getSendingLevel().setLevel(tuple.get(QProcessingDocument.processingDocument.incomingDoc.sendingLevel.level));
          processingDocument.getIncomingDoc().getDocumentType().setId(tuple.get(QProcessingDocument.processingDocument.incomingDoc.documentType.id));
          processingDocument.getIncomingDoc().getDocumentType().setType(tuple.get(QProcessingDocument.processingDocument.incomingDoc.documentType.type));
          processingDocument.getIncomingDoc().getDistributionOrg().setId(tuple.get(QProcessingDocument.processingDocument.incomingDoc.distributionOrg.id));
          processingDocument.getIncomingDoc().getDistributionOrg().setName(tuple.get(QProcessingDocument.processingDocument.incomingDoc.distributionOrg.name));
          return processingDocument;
        }).toList();
  }
}
