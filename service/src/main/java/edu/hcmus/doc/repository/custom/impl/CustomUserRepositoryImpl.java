package edu.hcmus.doc.repository.custom.impl;

import com.querydsl.jpa.impl.JPAQuery;
import edu.hcmus.doc.model.entity.QDocRoleEntity;
import edu.hcmus.doc.model.entity.QUser;
import edu.hcmus.doc.model.entity.QUserRole;
import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.repository.custom.CustomUserRepository;
import edu.hcmus.doc.repository.custom.DocAbstractCustomRepository;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CustomUserRepositoryImpl extends DocAbstractCustomRepository<User> implements CustomUserRepository {

  @Override
  public List<User> getUsers(String query, long first, long max) {
    JPAQuery<User> userJPAQuery = selectFrom(QUser.user);
    if (StringUtils.isNotBlank(query)) {
      userJPAQuery.where(QUser.user.username.startsWithIgnoreCase(query)
          .or(QUser.user.email.startsWithIgnoreCase(query)));
    }

    return userJPAQuery
        .innerJoin(QUser.user.roles, QUserRole.userRole)
        .fetchJoin()
        .innerJoin(QUserRole.userRole.role, QDocRoleEntity.docRoleEntity)
        .fetchJoin()
        .offset(first)
        .limit(max)
        .distinct()
        .fetch();
  }
}
