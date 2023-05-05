package edu.hcmus.doc.mainservice.controller;

import edu.hcmus.doc.mainservice.DocURL;
import edu.hcmus.doc.mainservice.model.dto.DepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.dto.UserSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.service.DepartmentService;
import edu.hcmus.doc.mainservice.service.UserService;
import edu.hcmus.doc.mainservice.util.mapper.DepartmentMapper;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  private final DepartmentMapper departmentMapper;

  @PostMapping("/search/users")
  public DocPaginationDto<UserDto> searchUsers(
      @RequestBody UserSearchCriteria userSearchCriteria,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int pageSize
  ) {
    return userService.searchUsers(userSearchCriteria, page, pageSize);
  }

  @GetMapping("/selection/departments")
  public List<DepartmentDto> getDepartmentsForSelection() {
    return departmentService.findAll()
        .stream()
        .map(departmentMapper::toDto)
        .toList();
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public Long createUser(@RequestBody @Valid UserDto userDto) {
    User user = userDecoratorMapper.toEntity(userDto);
    return userService.createUser(user);
  }

  @PutMapping("/users/{id}")
  public Long updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
    User user = userDecoratorMapper.partialUpdate(userDto, userService.getUserById(id));
    return userService.updateUser(user);
  }
}
