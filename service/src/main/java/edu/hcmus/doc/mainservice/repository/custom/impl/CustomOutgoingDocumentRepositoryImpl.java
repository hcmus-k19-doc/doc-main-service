package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QOutgoingDocument.outgoingDocument;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.OutgoingDocSearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.OutgoingDocument;
import edu.hcmus.doc.mainservice.model.entity.QDepartment;
import edu.hcmus.doc.mainservice.model.entity.QDocumentType;
import edu.hcmus.doc.mainservice.model.entity.QFolder;
import edu.hcmus.doc.mainservice.repository.custom.CustomOutgoingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CustomOutgoingDocumentRepositoryImpl
    extends DocAbstractCustomRepository<OutgoingDocument>
    implements CustomOutgoingDocumentRepository {

  @Override
  public long getTotalElements(OutgoingDocSearchCriteriaDto searchCriteriaDto) {
    return buildSearchQuery(searchCriteriaDto)
        .select(outgoingDocument.id.count())
        .fetchFirst();
  }

  @Override
  public long getTotalPages(OutgoingDocSearchCriteriaDto searchCriteriaDto, long limit) {
    long totalElements = getTotalElements(searchCriteriaDto);
    return (totalElements / limit) + (totalElements % limit == 0 ? 0 : 1);
  }

  @Override
  public List<OutgoingDocument> searchByCriteria(OutgoingDocSearchCriteriaDto searchCriteriaDto,
      long offset, long limit) {
    return
        buildSearchQuery(searchCriteriaDto)
            .orderBy(outgoingDocument.id.asc())
            .offset(offset * limit)
            .limit(limit)
            .fetch();
  }

  @Override
  public JPAQuery<OutgoingDocument> buildSearchQuery(
      OutgoingDocSearchCriteriaDto searchCriteriaDto) {
    BooleanBuilder where = new BooleanBuilder();

    if (searchCriteriaDto != null && StringUtils.isNotBlank(
        searchCriteriaDto.getOutgoingNumber())) {
      where.and(outgoingDocument.outgoingNumber.eq(searchCriteriaDto.getOutgoingNumber()));
    }
    if (searchCriteriaDto != null && searchCriteriaDto.getDocumentTypeId() != null) {
      where.and(outgoingDocument.documentType.id.eq(searchCriteriaDto.getDocumentTypeId()));
    }
    if (searchCriteriaDto != null
        && searchCriteriaDto.getReleaseDateFrom() != null
        && searchCriteriaDto.getReleaseDateTo() != null) {
      where.and(outgoingDocument.releaseDate.between(
          searchCriteriaDto.getReleaseDateFrom().atStartOfDay().toLocalDate(),
          searchCriteriaDto.getReleaseDateTo().plusDays(1).atStartOfDay().toLocalDate()
      ));
    }

    if (searchCriteriaDto != null && StringUtils.isNotBlank(searchCriteriaDto.getSummary())) {
      where.and(outgoingDocument.summary.startsWithIgnoreCase(searchCriteriaDto.getSummary()));
    }

    return selectFrom(outgoingDocument)
        .innerJoin(outgoingDocument.documentType, QDocumentType.documentType)
        .innerJoin(outgoingDocument.publishingDepartment, QDepartment.department)
        .innerJoin(outgoingDocument.folder, QFolder.folder)
        .distinct()
        .where(where);
  }
}
