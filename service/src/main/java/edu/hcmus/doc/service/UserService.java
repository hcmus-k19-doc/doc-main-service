package edu.hcmus.doc.service;

import edu.hcmus.doc.model.entity.User;
import java.util.List;

public interface UserService {

  List<User> getUsers();

  User getUserById(Long id);

  User getUserByUsername(String username);
}
