package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.User;
import edu.hcmus.doc.mainservice.model.enums.DocSystemRoleEnum;
import java.util.List;
import java.util.Optional;

public interface CustomUserRepository {

  List<User> getUsers(String query, long first, long max);

  Optional<User> getUserById(Long id);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  List<User> getUsersByRole(DocSystemRoleEnum role);
}
