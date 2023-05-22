package edu.hcmus.doc.mainservice.repository.custom;

import com.querydsl.core.Tuple;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocumentSearchResultDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailResponse;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.repository.DocAbstractSearchRepository;
import java.util.List;

public interface CustomProcessingDocumentRepository
    extends DocAbstractSearchRepository<ProcessingDocument, SearchCriteriaDto> {

  List<ProcessingDocument> findProcessingDocumentsByElasticSearchResult(
      List<IncomingDocumentSearchResultDto> incomingDocumentSearchResultDtoList, long offset,
      long limit);

  List<ProcessingDocument> findAllByIds(List<Long> ids);

  List<ProcessingDocument> findAllOutgoingByIds(List<Long> ids);

  GetTransferDocumentDetailResponse getTransferDocumentDetail(GetTransferDocumentDetailRequest request);

  List<Long> getListOfUserIdRelatedToTransferredDocument(Long processingDocumentId, Integer step, ProcessingDocumentRoleEnum role);

  GetTransferDocumentDetailResponse getTransferOutgoingDocumentDetail(GetTransferDocumentDetailRequest request);

  Tuple getCurrentStep(Long processingDocumentId);

}
