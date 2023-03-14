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
    return selectFrom(QProcessingDocument.processingDocument)
        .select(QProcessingDocument.processingDocument.id.count())
        .fetchOne();
  }

  @Override
  public List<ProcessingDocument> getProcessingDocuments(String query, long offset, long limit) {
    return select(
        QProcessingDocument.processingDocument.id,
        QProcessingDocument.processingDocument.status,
        QProcessingDocument.processingDocument.processingDuration,
        QIncomingDocument.incomingDocument.id,
        QIncomingDocument.incomingDocument.incomingNumber,
        QIncomingDocument.incomingDocument.originalSymbolNumber,
        QIncomingDocument.incomingDocument.arrivingDate,
        QIncomingDocument.incomingDocument.summary,
        QIncomingDocument.incomingDocument.sendingLevel.id,
        QIncomingDocument.incomingDocument.sendingLevel.level,
        QIncomingDocument.incomingDocument.documentType.id,
        QIncomingDocument.incomingDocument.documentType.type,
        QIncomingDocument.incomingDocument.distributionOrg.id,
        QIncomingDocument.incomingDocument.distributionOrg.name)
        .from(QProcessingDocument.processingDocument)
        .innerJoin(QProcessingDocument.processingDocument.incomingDoc, QIncomingDocument.incomingDocument)
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
          processingDocument.getIncomingDoc().setId(tuple.get(QIncomingDocument.incomingDocument.id));
          processingDocument.getIncomingDoc().setIncomingNumber(tuple.get(QIncomingDocument.incomingDocument.incomingNumber));
          processingDocument.getIncomingDoc().setOriginalSymbolNumber(tuple.get(QIncomingDocument.incomingDocument.originalSymbolNumber));
          processingDocument.getIncomingDoc().setArrivingDate(tuple.get(QIncomingDocument.incomingDocument.arrivingDate));
          processingDocument.getIncomingDoc().setSummary(tuple.get(QIncomingDocument.incomingDocument.summary));
          processingDocument.getIncomingDoc().getSendingLevel().setId(tuple.get(QIncomingDocument.incomingDocument.sendingLevel.id));
          processingDocument.getIncomingDoc().getSendingLevel().setLevel(tuple.get(QIncomingDocument.incomingDocument.sendingLevel.level));
          processingDocument.getIncomingDoc().getDocumentType().setId(tuple.get(QIncomingDocument.incomingDocument.documentType.id));
          processingDocument.getIncomingDoc().getDocumentType().setType(tuple.get(QIncomingDocument.incomingDocument.documentType.type));
          processingDocument.getIncomingDoc().getDistributionOrg().setId(tuple.get(QIncomingDocument.incomingDocument.distributionOrg.id));
          processingDocument.getIncomingDoc().getDistributionOrg().setName(tuple.get(QIncomingDocument.incomingDocument.distributionOrg.name));
          return processingDocument;
        }).toList();
  }
}
