package edu.hcmus.doc.mainservice.service.impl;

import static edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum.GIAM_DOC;
import static edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum.VAN_THU;
import static edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum.CHUYEN_VIEN;
import static edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum.TRUONG_PHONG;

import static edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException.INCOMING_DOCUMENT_NOT_FOUND;
import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.createProcessingDocument;
import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.createProcessingUser;
import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.createProcessingUserRole;
import static edu.hcmus.doc.mainservice.util.TransferDocumentUtils.getStep;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hcmus.doc.mainservice.model.dto.Attachment.AttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.IncomingDocumentWithAttachmentPostDto;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentMenuConfig;
import edu.hcmus.doc.mainservice.model.dto.IncomingDocument.TransferDocumentModalSettingDto;
import edu.hcmus.doc.mainservice.model.dto.SearchCriteriaDto;
import edu.hcmus.doc.mainservice.model.dto.TransferDocument.TransferDocDto;
import edu.hcmus.doc.mainservice.model.entity.Folder;
import edu.hcmus.doc.mainservice.model.entity.IncomingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUser;
import edu.hcmus.doc.mainservice.model.entity.ProcessingUserRole;
import edu.hcmus.doc.mainservice.model.entity.ReturnRequest;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.MESSAGE;
import edu.hcmus.doc.mainservice.model.enums.ProcessingDocumentRoleEnum;
import edu.hcmus.doc.mainservice.model.enums.ProcessingStatus;
import edu.hcmus.doc.mainservice.model.enums.TransferDocumentComponent;
import edu.hcmus.doc.mainservice.model.enums.TransferDocumentType;
import edu.hcmus.doc.mainservice.model.exception.DocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.FolderNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.IncomingDocumentNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserNotFoundException;
import edu.hcmus.doc.mainservice.model.exception.UserRoleNotFoundException;
import edu.hcmus.doc.mainservice.repository.FolderRepository;
import edu.hcmus.doc.mainservice.repository.IncomingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingDocumentRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRepository;
import edu.hcmus.doc.mainservice.repository.ProcessingUserRoleRepository;
import edu.hcmus.doc.mainservice.repository.ReturnRequestRepository;
import edu.hcmus.doc.mainservice.repository.UserRepository;
import edu.hcmus.doc.mainservice.security.util.SecurityUtils;
import edu.hcmus.doc.mainservice.service.AttachmentService;
import edu.hcmus.doc.mainservice.service.FolderService;
import edu.hcmus.doc.mainservice.service.IncomingDocumentService;
import edu.hcmus.doc.mainservice.util.DocObjectUtils;
import edu.hcmus.doc.mainservice.util.ResourceBundleUtils;
import edu.hcmus.doc.mainservice.util.TransferDocumentUtils;
import edu.hcmus.doc.mainservice.util.mapper.IncomingDocumentMapper;
import edu.hcmus.doc.mainservice.util.mapper.decorator.AttachmentMapperDecorator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class IncomingDocumentServiceImpl implements IncomingDocumentService {

  private final IncomingDocumentRepository incomingDocumentRepository;

  private final FolderService folderService;

  private final AttachmentService attachmentService;

  private final ObjectMapper objectMapper;

  private final IncomingDocumentMapper incomingDecoratorDocumentMapper;

  private final AttachmentMapperDecorator attachmentMapperDecorator;

  private final ProcessingDocumentRepository processingDocumentRepository;

  private final ProcessingUserRepository processingUserRepository;

  private final ProcessingUserRoleRepository processingUserRoleRepository;

  private final UserRepository userRepository;

  private final ReturnRequestRepository returnRequestRepository;

  private final FolderRepository folderRepository;

  @Override
  public long getTotalElements(SearchCriteriaDto searchCriteriaDto) {
    return processingDocumentRepository.getTotalElements(searchCriteriaDto);
  }

  @Override
  public IncomingDocument updateIncomingDocument(IncomingDocument incomingDocument) {
    IncomingDocument updatingIncomingDocument = getIncomingDocumentById(incomingDocument.getId());
    DocObjectUtils.copyNonNullProperties(incomingDocument, updatingIncomingDocument);
    return incomingDocumentRepository.saveAndFlush(updatingIncomingDocument);
  }

  @Override
  public IncomingDocument createIncomingDocument(IncomingDocument incomingDocument) {
    Folder folder = folderRepository.findById(incomingDocument.getFolder().getId())
        .orElseThrow(() -> new FolderNotFoundException(FolderNotFoundException.FOLDER_NOT_FOUND));
    folder.setNextNumber(folder.getNextNumber() + 1);

    return incomingDocumentRepository.save(incomingDocument);
  }

  @Override
  public long getTotalPages(SearchCriteriaDto searchCriteriaDto, long limit) {
    return getTotalElements(searchCriteriaDto) / limit;
  }

  @Override
  public IncomingDocument getIncomingDocumentById(Long id) {
    IncomingDocument incomingDocument = incomingDocumentRepository.getIncomingDocumentById(id);

    if (ObjectUtils.isEmpty(incomingDocument)) {
      throw new DocumentNotFoundException(DocumentNotFoundException.DOCUMENT_NOT_FOUND);
    }

    return incomingDocument;
  }

  @Override
  public List<ProcessingDocument> searchIncomingDocuments(SearchCriteriaDto searchCriteria,
      int page, int pageSize) {
    return processingDocumentRepository.searchByCriteria(searchCriteria, page, pageSize);
  }

  @Override
  public List<IncomingDocument> getIncomingDocuments(String query, long offset, long limit) {
    return incomingDocumentRepository.getIncomingDocuments(query, offset, limit);
  }

  @Override
  public IncomingDocument findById(Long id) {
    return incomingDocumentRepository.findById(id).orElseThrow(
        () -> new IncomingDocumentNotFoundException(INCOMING_DOCUMENT_NOT_FOUND)
    );
  }

  @Override
  public IncomingDocument createIncomingDocument(
      IncomingDocumentWithAttachmentPostDto incomingDocumentWithAttachmentPostDto)
      throws JsonProcessingException {
    IncomingDocumentPostDto incomingDocumentPostDto = objectMapper.readValue(
        incomingDocumentWithAttachmentPostDto.getIncomingDocumentPostDto(),
        IncomingDocumentPostDto.class);
    IncomingDocument incomingDocument = incomingDecoratorDocumentMapper.toEntity(
        incomingDocumentPostDto);

    Folder folder = folderService.findById(incomingDocument.getFolder().getId());
    folder.setNextNumber(folder.getNextNumber() + 1);
    IncomingDocument savedIncomingDocument = incomingDocumentRepository.save(incomingDocument);

    AttachmentPostDto attachmentPostDto = attachmentMapperDecorator.toAttachmentPostDto(
        savedIncomingDocument.getId(), incomingDocumentWithAttachmentPostDto.getAttachments());

    attachmentService.saveAttachmentsByIncomingDocId(
        attachmentPostDto);
    return savedIncomingDocument;
  }

  @Override
  public void transferDocuments(TransferDocDto transferDocDto) {
    User currentUser = SecurityUtils.getCurrentUser();
    User reporter = getUserByIdOrThrow(transferDocDto.getReporterId());
    User assignee = getUserByIdOrThrow(transferDocDto.getAssigneeId());

    if (transferDocDto.getIsTransferToSameLevel()) {
      transferToSameLevel(transferDocDto, reporter, assignee, currentUser.getRole());
      return;
    }

    List<User> collaborators = userRepository.findAllById(
        Objects.requireNonNull(transferDocDto.getCollaboratorIds()));

    if (transferDocDto.getTransferDocumentType() == TransferDocumentType.TRANSFER_TO_GIAM_DOC
        && currentUser.getRole() == VAN_THU) {
      // neu la van thu chuyen cho giam doc => new document
      transferNewDocuments(transferDocDto, reporter, assignee, collaborators);
    } else {
      transferExistedDocuments(transferDocDto, reporter, assignee, collaborators);
    }
  }

  private void transferToSameLevel(TransferDocDto transferDocDto, User reporter, User assignee,
      DocSystemRoleEnum role) {
    if (role == VAN_THU) {
      List<IncomingDocument> incomingDocuments = incomingDocumentRepository
          .getIncomingDocumentsByIds(transferDocDto.getDocumentIds());

      // update the created_by field of incoming documents
      incomingDocuments.forEach(incomingDocument -> {
        incomingDocument.setCreatedBy(assignee.getId().toString());
        incomingDocumentRepository.save(incomingDocument);
      });
    } else {
      List<ProcessingDocument> processingDocuments = processingDocumentRepository
          .findAllByIds(transferDocDto.getDocumentIds());

      // update the user_id field of processing_user
      processingDocuments.forEach(processingDocument -> {
        List<ProcessingUser> processingUserList = processingUserRepository.findAllByUserIdAndProcessingDocumentId(
            reporter.getId(),
            processingDocument.getId());
        processingUserList.forEach(processingUser -> {
          processingUser.setUser(assignee);
          processingUserRepository.save(processingUser);
        });
      });
    }
  }

  private void transferNewDocuments(TransferDocDto transferDocDto, User reporter,
      User assignee, List<User> collaborators) {

    List<IncomingDocument> incomingDocuments = incomingDocumentRepository
        .getIncomingDocumentsByIds(transferDocDto.getDocumentIds());

    ReturnRequest returnRequest = returnRequestRepository.findById(1L).orElseThrow(
        () -> new RuntimeException("Return request not found")
    );

    int step = getStep(reporter, assignee);

    // TODO: validate incomingDocuments to make sure they are not processed yet.
    // save processing documents with status IN_PROGRESS
    incomingDocuments.forEach(incomingDocument -> {
      ProcessingDocument processingDocument = createProcessingDocument(incomingDocument,
          ProcessingStatus.IN_PROGRESS);

      ProcessingDocument savedProcessingDocument = processingDocumentRepository.save(
          processingDocument);

      saveCollaboratorList(savedProcessingDocument, collaborators, returnRequest, transferDocDto,
          step);

      saveReporterOrAssignee(savedProcessingDocument, assignee, returnRequest, transferDocDto, step,
          ProcessingDocumentRoleEnum.ASSIGNEE);

      saveReporterOrAssignee(savedProcessingDocument, reporter, returnRequest, transferDocDto, step,
          ProcessingDocumentRoleEnum.REPORTER);
    });
  }

  private void transferExistedDocuments(TransferDocDto transferDocDto, User reporter,
      User assignee, List<User> collaborators) {
    int step = getStep(reporter, assignee);
    List<ProcessingDocument> processingDocuments = processingDocumentRepository.findAllByIds(
        transferDocDto.getDocumentIds());

    ReturnRequest returnRequest = returnRequestRepository.findById(1L).orElseThrow(
        () -> new RuntimeException("Return request not found")
    );

    processingDocuments.forEach(processingDocument -> {

      saveCollaboratorList(processingDocument, collaborators, returnRequest, transferDocDto, step);

      saveReporterOrAssignee(processingDocument, assignee, returnRequest, transferDocDto, step,
          ProcessingDocumentRoleEnum.ASSIGNEE);

      saveReporterOrAssignee(processingDocument, reporter, returnRequest, transferDocDto, step,
          ProcessingDocumentRoleEnum.REPORTER);
    });
  }

  private void saveCollaboratorList(ProcessingDocument processingDocument, List<User> collaborators,
      ReturnRequest returnRequest, TransferDocDto transferDocDto, Integer step) {
    collaborators.forEach(collaborator -> {
      ProcessingUser processingUser1 = createProcessingUser(processingDocument, collaborator, step,
          returnRequest, transferDocDto);
      ProcessingUser savedProcessingUser1 = processingUserRepository.save(processingUser1);

      ProcessingUserRole processingUserRole1 = createProcessingUserRole(savedProcessingUser1,
          ProcessingDocumentRoleEnum.COLLABORATOR);
      processingUserRoleRepository.save(processingUserRole1);
    });
  }

  private void saveReporterOrAssignee(ProcessingDocument processingDocument, User user,
      ReturnRequest returnRequest, TransferDocDto transferDocDto, Integer step,
      ProcessingDocumentRoleEnum role) {
    ProcessingUser processingUser = createProcessingUser(processingDocument, user, step,
        returnRequest, transferDocDto);
    ProcessingUser savedProcessingUser = processingUserRepository.save(processingUser);

    ProcessingUserRole processingUserRole = createProcessingUserRole(savedProcessingUser, role);
    processingUserRoleRepository.save(processingUserRole);
  }

  private User getUserByIdOrThrow(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

  @Override
  public TransferDocumentModalSettingDto getTransferDocumentModalSetting() {
    TransferDocumentModalSettingDto settings = new TransferDocumentModalSettingDto();
    List<TransferDocumentMenuConfig> menuConfigs = new ArrayList<>();
    User currUser = SecurityUtils.getCurrentUser();
    settings.setCurrentRole(currUser.getRole());

    switch (currUser.getRole()) {
      case VAN_THU -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.submit_document_to_giam_doc_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.submit_document_to_giam_doc_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC)
            .isTransferToSameLevel(false)
            .build());
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_van_thu_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_van_thu_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_VAN_THU.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_VAN_THU)
            .isTransferToSameLevel(true)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC);
        settings.setDefaultComponentKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value);
      }
      case GIAM_DOC -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_giam_doc_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_giam_doc_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC)
            .isTransferToSameLevel(true)
            .build());
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.assign_document_to_truong_phong_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.assign_document_to_truong_phong_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG)
            .isTransferToSameLevel(false)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_GIAM_DOC);
        settings.setDefaultComponentKey(TransferDocumentComponent.TRANSFER_TO_GIAM_DOC.value);
      }
      case TRUONG_PHONG -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_truong_phong_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_truong_phong_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG)
            .isTransferToSameLevel(true)
            .build());
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.assign_document_to_chuyen_vien_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_CHUYEN_VIEN.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.assign_document_to_chuyen_vien_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_CHUYEN_VIEN.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_CHUYEN_VIEN)
            .isTransferToSameLevel(false)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_TRUONG_PHONG);
        settings.setDefaultComponentKey(TransferDocumentComponent.TRANSFER_TO_TRUONG_PHONG.value);
      }
      case CHUYEN_VIEN -> {
        menuConfigs.add(TransferDocumentMenuConfig.builder()
            .transferDocumentTypeLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_chuyen_vien_type_label))
            .componentKey(TransferDocumentComponent.TRANSFER_TO_CHUYEN_VIEN.value)
            .menuLabel(ResourceBundleUtils.getContent(
                MESSAGE.transfer_document_to_chuyen_vien_menu_label))
            .menuKey(TransferDocumentComponent.TRANSFER_TO_CHUYEN_VIEN.value)
            .transferDocumentType(TransferDocumentType.TRANSFER_TO_CHUYEN_VIEN)
            .isTransferToSameLevel(true)
            .build());
        settings.setDefaultTransferDocumentType(TransferDocumentType.TRANSFER_TO_CHUYEN_VIEN);
        settings.setDefaultComponentKey(TransferDocumentComponent.TRANSFER_TO_CHUYEN_VIEN.value);
      }
      default -> {
        throw new UserRoleNotFoundException(UserRoleNotFoundException.USER_ROLE_NOT_FOUND);
      }
    }
    settings.setMenuConfigs(menuConfigs);
    return settings;
  }
}
