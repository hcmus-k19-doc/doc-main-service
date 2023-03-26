package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocAbstractSearchRepository<T> {

  List<T> searchByCriteria(SearchCriteriaDto criteria, long offset, long limit);

  Long getTotalElements(SearchCriteriaDto criteria);

  long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit);
}
