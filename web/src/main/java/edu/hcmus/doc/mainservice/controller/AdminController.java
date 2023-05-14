package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.DepartmentSearchCriteria;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeDto;
import edu.hcmus.doc.mainservice.model.dto.DocumentTypeSearchCriteria;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.dto.UserSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.Department;
import edu.hcmus.doc.mainservice.model.entity.DocumentType;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.service.DepartmentService;
import edu.hcmus.doc.mainservice.service.DocumentTypeService;
import edu.hcmus.doc.mainservice.service.UserService;
import edu.hcmus.doc.mainservice.util.mapper.DepartmentMapper;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/admin")
public class AdminController extends DocAbstractController {

  private final UserService userService;
  private final DepartmentService departmentService;
  private final DocumentTypeService documentTypeService;
  private final DepartmentMapper departmentMapper;

  @GetMapping("/selection/departments")
  public List<DepartmentDto> getDepartmentsForSelection() {
    return departmentService.findAll()
        .stream()
        .map(departmentMapper::toDto)
        .toList();
  }

  @PostMapping("/search/document-types")
  public DocPaginationDto<DocumentTypeDto> searchDocumentTypes(
      @RequestBody(required = false) DocumentTypeSearchCriteria documentTypeSearchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int pageSize
  ) {
    return documentTypeService.search(documentTypeSearchCriteria, page, pageSize);
  }

  @PostMapping("/search/users")
  public DocPaginationDto<UserDto> searchUsers(
      @RequestBody UserSearchCriteria userSearchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int pageSize
  ) {
    return userService.search(userSearchCriteria, page, pageSize);
  }

  @PostMapping("/search/departments")
  public DocPaginationDto<DepartmentDto> searchDepartments(
      @RequestBody DepartmentSearchCriteria departmentSearchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int pageSize
  ) {
    return departmentService.search(departmentSearchCriteria, page, pageSize);
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public Long createUser(@RequestBody @Valid UserDto userDto) {
    User user = userDecoratorMapper.toEntity(userDto);
    return userService.createUser(user);
  }

  @PostMapping("document-types")
  @ResponseStatus(HttpStatus.CREATED)
  public Long saveDocumentType(@RequestBody @Valid DocumentTypeDto documentTypeDto) {
    DocumentType documentType;
    if (documentTypeDto.getId() == null) {
      documentType = documentTypeMapper.toEntity(documentTypeDto);
    } else {
      documentType = documentTypeMapper.partialUpdate(documentTypeDto, documentTypeService.findById(documentTypeDto.getId()));
    }
    return documentTypeService.saveDocumentType(documentType).getId();
  }

  @PostMapping("/departments")
  @ResponseStatus(HttpStatus.CREATED)
  public Long saveDepartment(@RequestBody @Valid DepartmentDto departmentDto) {
    Department department;
    if (!departmentDto.isPersisted()) {
      department = departmentMapper.toEntity(departmentDto);
    } else {
      department = departmentMapper.partialUpdate(departmentDto, departmentService.getDepartmentById(departmentDto.getId()));
    }

    return departmentService.saveDepartment(department);
  }

  @PutMapping("/users/{id}")
  public Long updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
    User user = userDecoratorMapper.partialUpdate(userDto, userService.getUserById(id));
    return userService.updateUser(user);
  }

  @DeleteMapping("/users")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUsers(@RequestBody List<Long> userIds) {
    userService.deleteUsers(userIds);
  }

  @DeleteMapping("/document-types")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDocumentTypes(@RequestBody List<Long> documentTypeIds) {
    documentTypeService.deleteDocumentTypes(documentTypeIds);
  }
}
