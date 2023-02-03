package edu.hcmus.doc.util.mapper.impl;

import static java.util.stream.Collectors.toSet;

import edu.hcmus.doc.model.dto.UserDto;
import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.model.enums.DocRole;
import edu.hcmus.doc.util.mapper.UserMapper;
import edu.hcmus.doc.util.mapper.UserMapperDecorator;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UserMapperDecoratorImpl extends UserMapperDecorator {

  public UserMapperDecoratorImpl(@Qualifier("userMapperImpl") UserMapper userMapper) {
    super(userMapper);
  }

  @Override
  public UserDto toDto(User user) {
    UserDto userDto = userMapper.toDto(user);
    if (CollectionUtils.isNotEmpty(user.getRoles())) {
      userDto.setRoles(mapRoles(user));
    }
    return userDto;
  }

  private Set<DocRole> mapRoles(User user) {
    return user
        .getRoles()
        .stream()
        .map(userRole -> userRole.getRole().getName())
        .collect(toSet());
  }
}
