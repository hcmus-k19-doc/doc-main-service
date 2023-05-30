package edu.hcmus.doc.mainservice.repository.custom.impl;

import static edu.hcmus.doc.mainservice.model.entity.QTransferHistory.transferHistory;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentHistoryResponse;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferHistorySearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.entity.TransferHistory;
import edu.hcmus.doc.mainservice.repository.custom.CustomTransferHistoryRepository;
import edu.hcmus.doc.mainservice.repository.custom.DocAbstractCustomRepository;
import java.util.List;
public class CustomTransferHistoryRepositoryImpl extends
    DocAbstractCustomRepository<TransferHistory>
    implements CustomTransferHistoryRepository {

  @Override
  public GetTransferDocumentHistoryResponse getTransferDocumentHistory(Long currentUserId) {
    return null;
  }

  @Override
  public long getTotalElements(TransferHistorySearchCriteriaDto criteria) {
    return 0;
  }

  @Override
  public long getTotalPages(TransferHistorySearchCriteriaDto criteria, long limit) {
    return 0;
  }

  @Override
  public List<TransferHistory> searchByCriteria(TransferHistorySearchCriteriaDto criteria,
      long offset, long limit) {
    return null;
  }

  @Override
  public JPAQuery<TransferHistory> buildSearchQuery(TransferHistorySearchCriteriaDto criteria) {
    return null;
  }

  private JPAQuery<TransferHistory> searchQueryByCriteria(TransferHistorySearchCriteriaDto criteria) {
    BooleanBuilder where = new BooleanBuilder();

    if(criteria != null && criteria.getUserId() != null) {
      where.and(transferHistory.sender.id .eq(criteria.getUserId())
          .or(transferHistory.receiver.id.eq(criteria.getUserId())));
    }

    JPAQuery<TransferHistory> query = selectFrom(transferHistory)
        .where(where)
        .orderBy(transferHistory.createdDate.desc());
    return query;
  }
}
