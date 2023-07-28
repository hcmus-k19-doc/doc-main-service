package edu.hcmus.doc.mainservice.util.mapper.decorator;

import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPutDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.GetTransferDocumentDetailRequest;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.*;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.DistributionOrganizationService;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.service.ProcessingDocumentService;
import edu.hcmus.doc.mainservice.util.DocMessageUtils;
import edu.hcmus.doc.mainservice.util.TransferDocumentUtils;
import edu.hcmus.doc.mainservice.util.mapper.IncomingDocumentMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class IncomingDocumentMapperDecorator implements IncomingDocumentMapper {

  @Autowired
  DocumentTypeService documentTypeService;

  @Autowired
  DistributionOrganizationService distributionOrganizationService;

  @Autowired
  FolderService folderService;

  @Autowired
  AttachmentService attachmentService;

  @Autowired
  ProcessingDocumentService processingDocumentService;

  @Autowired
  ProcessingDocumentRepository processingDocumentRepository;

  @Autowired
  IncomingDocumentService incomingDocumentService;


  @Autowired
  @Qualifier("delegate")
  private IncomingDocumentMapper delegate;

  @Override
  public IncomingDocumentDto toDto(IncomingDocument incomingDocument) {
    List<AttachmentDto> attachments = attachmentService.getAttachmentsByDocId(
        incomingDocument.getId(), ParentFolderEnum.ICD);

    IncomingDocumentDto dto = delegate.toDto(incomingDocument);
    User currentUser = SecurityUtils.getCurrentUser();

    int step = TransferDocumentUtils.getStep(currentUser, null, true);
    Boolean isDocTransferred = processingDocumentService.isUserWorkingOnDocumentWithSpecificRole(
        GetTransferDocumentDetailRequest.builder()
            .documentId(incomingDocument.getId())
            .userId(currentUser.getId())
            .role(ProcessingDocumentRoleEnum.REPORTER)
            .step(step)
            .build());

    ProcessingStatus status = processingDocumentService.getProcessingStatus(
        incomingDocument.getId());

    int collabStep = TransferDocumentUtils.getStep(currentUser, null, false);
    Boolean isDocCollaborator = processingDocumentService.isUserWorkingOnDocumentWithSpecificRole(
        GetTransferDocumentDetailRequest.builder()
            .documentId(incomingDocument.getId())
            .userId(currentUser.getId())
            .role(ProcessingDocumentRoleEnum.COLLABORATOR)
            .step(collabStep)
            .build());

    dto.setStatus(status);
    dto.setIsDocTransferred(isDocTransferred);
    dto.setIsDocCollaborator(isDocCollaborator);
    dto.setIsTransferable(processingDocumentRepository.getIncomingDocumentsWithTransferPermission()
        .contains(incomingDocument.getId()));
    dto.setIsCloseable(incomingDocumentService.validateCloseDocument(incomingDocument.getId()));
    dto.setAttachments(attachments);

    dto.setProcessingDuration(
        processingDocumentService
            .getDateExpired(incomingDocument.getId(), currentUser.getId(), currentUser.getRole(),
                false)
            .orElse(null)
    );

    dto.setIsDocTransferredByNextUserInFlow(processingDocumentService.isExistUserWorkingOnThisDocumentAtSpecificStep(
        incomingDocument.getId(), step + 1, ProcessingDocumentTypeEnum.INCOMING_DOCUMENT));
    return dto;
  }

  @Override
  public IncomingDocumentDto toDto(ProcessingDocument processingDocument) {
    List<AttachmentDto> attachments = attachmentService.getAttachmentsByDocId(
        processingDocument.getIncomingDoc().getId(), ParentFolderEnum.ICD);

    IncomingDocumentDto dto = delegate.toDto(processingDocument.getIncomingDoc());
    dto.setStatus(processingDocument.getStatus());
    dto.setAttachments(attachments);

    User currentUser = SecurityUtils.getCurrentUser();
    int step = TransferDocumentUtils.getStep(currentUser, null, true);
    Boolean isDocTransferred = processingDocumentService.isUserWorkingOnDocumentWithSpecificRole(
        GetTransferDocumentDetailRequest.builder()
            .documentId(processingDocument.getIncomingDoc().getId())
            .userId(currentUser.getId())
            .role(ProcessingDocumentRoleEnum.REPORTER)
            .step(step)
            .build());
    dto.setIsDocTransferred(isDocTransferred);

    int collabStep = TransferDocumentUtils.getStep(currentUser, null, false);
    Boolean isDocCollaborator = processingDocumentService.isUserWorkingOnDocumentWithSpecificRole(
        GetTransferDocumentDetailRequest.builder()
            .documentId(processingDocument.getIncomingDoc().getId())
            .userId(currentUser.getId())
            .role(ProcessingDocumentRoleEnum.COLLABORATOR)
            .step(collabStep)
            .build());

    dto.setIsDocCollaborator(isDocCollaborator);
    dto.setIsTransferable(processingDocumentRepository.getIncomingDocumentsWithTransferPermission()
        .contains(processingDocument.getIncomingDoc().getId()));

    dto.setCustomProcessingDuration(
        processingDocumentService
            .getDateExpiredV2(processingDocument.getIncomingDoc().getId(), currentUser.getId(),
                currentUser.getRole(), true, ProcessingDocumentTypeEnum.INCOMING_DOCUMENT)
            .map(result -> result.equals("infinite") ? DocMessageUtils.getContent(
                MESSAGE.infinite_processing_duration) : LocalDate.parse(result).format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")))
            .orElse(null)
    );

    dto.setIsDocTransferredByNextUserInFlow(processingDocumentService.isExistUserWorkingOnThisDocumentAtSpecificStep(
        processingDocument.getIncomingDoc().getId(), step + 1, ProcessingDocumentTypeEnum.INCOMING_DOCUMENT));

    return dto;
  }

  @Override
  public IncomingDocument toEntity(IncomingDocumentPostDto dto) {
    IncomingDocument entity = delegate.toEntity(dto);

    entity.setFolder(folderService.findById(dto.getFolder()));
    entity.setDocumentType(documentTypeService.findById(dto.getDocumentType()));
    entity.setDistributionOrg(distributionOrganizationService.findById(dto.getDistributionOrg()));
    entity.setSendingLevel(null);

    return entity;
  }

  @Override
  public IncomingDocument toEntity(IncomingDocumentPutDto dto) {
    IncomingDocument entity = delegate.toEntity(dto);

    entity.setFolder(folderService.findById(dto.getFolder()));
    entity.setDocumentType(documentTypeService.findById(dto.getDocumentType()));
    entity.setDistributionOrg(distributionOrganizationService.findById(dto.getDistributionOrg()));
    entity.setSendingLevel(null);

    return entity;
  }
}

