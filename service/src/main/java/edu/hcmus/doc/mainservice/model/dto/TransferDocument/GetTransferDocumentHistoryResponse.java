package edu.hcmus.doc.mainservice.model.dto.TransferDocument;

import java.util.List;
import lombok.Data;

@Data
public class GetTransferDocumentHistoryResponse {

  private List<TransferHistoryDetailDto> transferHistoryDetailDtos;
}
