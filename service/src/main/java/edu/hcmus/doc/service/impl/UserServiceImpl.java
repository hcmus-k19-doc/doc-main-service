package edu.hcmus.doc.service.impl;

import edu.hcmus.doc.model.entity.User;
import edu.hcmus.doc.repository.UserRepository;
import edu.hcmus.doc.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public List<User> getUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElseGet(User::new);
  }

  @Override
  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public long getTotalUsers() {
    return userRepository.count();
  }

  @Override
  public boolean validateUserCredentialsByUserId(Long id, String password) {
    return passwordEncoder.matches(password, userRepository.findById(id).orElseThrow().getPassword());
  }
}
