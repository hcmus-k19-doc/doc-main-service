package edu.hcmus.doc.mainservice.service;

import edu.hcmus.doc.mainservice.model.dto.DocPaginationDto;
import edu.hcmus.doc.mainservice.model.dto.UserDepartmentDto;
import edu.hcmus.doc.mainservice.model.dto.UserDto;
import edu.hcmus.doc.mainservice.model.dto.UserSearchCriteria;
import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.util.List;

public interface UserService {

  List<User> getUsers(String query, long first, long max);

  User getUserById(Long id);

  User getUserByUsername(String username);

  User getUserByEmail(String email);

  long getTotalUsers();

  boolean validateUserCredentialsByUserId(Long id, String password);

  User getCurrentUserFromDB();

  List<User> getUsersByRole(DocSystemRoleEnum role);

  List<UserDepartmentDto> getUsersByRoleWithDepartment(DocSystemRoleEnum role);

  Long createUser(User user);

  Long updateUser(User user);

  Long updateCurrentUserPassword(String oldPassword, String newPassword);

  DocPaginationDto<UserDto> searchUsers(UserSearchCriteria userSearchCriteria, int page,
      int pageSize);
}
