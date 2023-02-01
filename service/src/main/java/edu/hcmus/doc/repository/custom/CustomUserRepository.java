package edu.hcmus.doc.repository.custom;

import edu.hcmus.doc.model.entity.User;
import java.util.List;

public interface CustomUserRepository {

  List<User> getUsers(String query, long first, long max);
}
