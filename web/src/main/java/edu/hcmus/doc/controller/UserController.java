package edu.hcmus.doc.controller;

import edu.hcmus.doc.DocURL;
import edu.hcmus.doc.model.dto.UserDto;
import edu.hcmus.doc.model.entity.DocRoleEntity;
import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.security.util.SecurityUtils;
import edu.hcmus.doc.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/users")
public class UserController extends DocAbstractController {

  private final UserService userService;

  @GetMapping
  public List<UserDto> getUsers() {
    return userService
        .getUsers()
        .stream()
        .map(user -> {
          UserDto dto = userMapper.toDto(user);
          dto.setRoles(user
              .getRoles()
              .stream()
              .map(DocRoleEntity::getName)
              .collect(Collectors.toSet()));
          return dto;
        })
        .toList();
  }

  @GetMapping("/{id}")
  public UserDto getUserById(@PathVariable Long id) {
    User user = userService.getUserById(id);
    UserDto dto = userMapper.toDto(user);
    dto.setRoles(user
        .getRoles()
        .stream()
        .map(DocRoleEntity::getName)
        .collect(Collectors.toSet()));
    return dto;
  }

  @GetMapping("/username/{username}")
  public UserDto getUserByUsername(@PathVariable String username) {
    return userMapper.toDto(userService.getUserByUsername(username));
  }

  @GetMapping("/current-name")
  public String getCurrentName() {
    return SecurityUtils.getCurrentName();
  }
}
