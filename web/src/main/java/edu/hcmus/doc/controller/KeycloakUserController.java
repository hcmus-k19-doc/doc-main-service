package edu.hcmus.doc.controller;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import edu.hcmus.doc.DocURL;
import edu.hcmus.doc.model.dto.CredentialsDto;
import edu.hcmus.doc.model.dto.UserDto;
import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.security.util.SecurityUtils;
import edu.hcmus.doc.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(DocURL.API_V1 + "/keycloak/users")
public class KeycloakUserController extends DocAbstractController {

  private final UserService userService;

  @GetMapping
  public List<UserDto> getUsers(
      @RequestParam(required = false) String query,
      @RequestParam(required = false, defaultValue = "0") long first,
      @RequestParam(required = false, defaultValue = "10") long max) {
    return userService
        .getUsers(query, first, max)
        .stream()
        .map(user -> {
          UserDto dto = userMapper.toDto(user);
          dto.setRoles(user.getRoles().stream().map(userRole -> userRole.getRole().getName()).collect(Collectors.toSet()));
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
        .map(userRole -> userRole.getRole().getName())
        .collect(Collectors.toSet()));
    return dto;
  }

  @PostMapping(value = "/{id}/auth/validate-credentials", consumes = APPLICATION_FORM_URLENCODED_VALUE)
  public boolean getCredentials(@PathVariable Long id, @RequestBody CredentialsDto credentialsDto) {
    return userService.validateUserCredentialsByUserId(id, credentialsDto.getPassword());
  }

  @GetMapping("/username/{username}")
  public UserDto getUserByUsername(@PathVariable String username) {
    return userMapper.toDto(userService.getUserByUsername(username));
  }

  @GetMapping("/email/{email}")
  public UserDto getUserByEmail(@PathVariable String email) {
    return userMapper.toDto(userService.getUserByEmail(email));
  }

  @GetMapping("/total-users")
  public long getTotalUsers() {
    return userService.getTotalUsers();
  }

  @GetMapping("/current-name")
  public String getCurrentName() {
    return SecurityUtils.getCurrentName();
  }
}
