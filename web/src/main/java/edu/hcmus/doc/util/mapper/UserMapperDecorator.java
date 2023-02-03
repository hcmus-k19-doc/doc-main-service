package edu.hcmus.doc.util.mapper;

import edu.hcmus.doc.model.dto.UserDto;
import edu.hcmus.doc.model.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class UserMapperDecorator implements UserMapper {

  protected final UserMapper userMapper;

  @Override
  public UserDto toDto(User user) {
    return userMapper.toDto(user);
  }
}
