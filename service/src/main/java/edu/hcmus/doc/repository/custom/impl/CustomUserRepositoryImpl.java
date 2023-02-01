package edu.hcmus.doc.repository.custom.impl;

import edu.hcmus.doc.model.entity.QUser;
import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.repository.custom.CustomUserRepository;
import edu.hcmus.doc.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CustomUserRepositoryImpl extends DocAbstractCustomRepository<User> implements CustomUserRepository {

  @Override
  public List<User> getUsers(String query, long first, long max) {
    if (StringUtils.isBlank(query)) {
      return selectFrom(QUser.user)
          .offset(first)
          .limit(max)
          .fetch();
    }

    return selectFrom(QUser.user)
        .where(QUser.user.username.startsWith(query).or(QUser.user.email.startsWith(query)))
        .offset(first)
        .limit(max)
        .fetch();
  }
}
