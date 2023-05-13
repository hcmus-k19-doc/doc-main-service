package edu.hcmus.doc.mainservice.util;

import static edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum.CHUYEN_VIEN;
import static edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum.GIAM_DOC;
import static edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum.TRUONG_PHONG;
import static edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum.VAN_THU;

import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class TransferDocumentUtils {

  public static int getStep(User reporter, User assignee) {
    int step;
    if (reporter.getRole() == VAN_THU && assignee.getRole() == GIAM_DOC) {
      step = 1;
    } else if (reporter.getRole() == GIAM_DOC && assignee.getRole() == TRUONG_PHONG) {
      step = 2;
    } else if (reporter.getRole() == TRUONG_PHONG && assignee.getRole() == CHUYEN_VIEN) {
      step = 3;
    } else {
      step = 0;
    }
    return step;
  }

  public static ProcessingDocument createProcessingDocument(IncomingDocument incomingDocument,
      ProcessingStatus processingStatus) {
    ProcessingDocument processingDocument = new ProcessingDocument();
    processingDocument.setIncomingDoc(incomingDocument);
    processingDocument.setStatus(processingStatus);
    processingDocument.setOpened(true);
    processingDocument.setProcessingRequest("processing_request");
    return processingDocument;
  }

  public static ProcessingUser createProcessingUser(ProcessingDocument processingDocument,
      User user,
      Integer step, ReturnRequest returnRequest, TransferDocDto transferDocDto) {
    ProcessingUser processingUser = new ProcessingUser();
    processingUser.setProcessingDocument(processingDocument);
    processingUser.setUser(user);
    processingUser.setStep(step);
    processingUser.setReturnRequest(returnRequest);
    processingUser.setProcessMethod(transferDocDto.getProcessMethod());

    if (Boolean.FALSE.equals(transferDocDto.getIsInfiniteProcessingTime())) {
      processingUser.setProcessingDuration(LocalDate.parse(
          Objects.requireNonNull(transferDocDto.getProcessingTime()),
          DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    return processingUser;
  }

  public static ProcessingUserRole createProcessingUserRole(ProcessingUser processingUser,
      ProcessingDocumentRoleEnum role) {
    ProcessingUserRole processingUserRole = new ProcessingUserRole();
    processingUserRole.setProcessingUser(processingUser);
    processingUserRole.setRole(role);

    return processingUserRole;
  }

}
