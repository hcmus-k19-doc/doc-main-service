package edu.hcmus.doc.mainservice.model.entity;

import java.util.Collection;
import java.util.Collections;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Table(name = "user", schema = "doc_main", catalog = "doc")
public class User extends DocAbstractEntity implements UserDetails {

  @Column(name = "first_name", columnDefinition = "VARCHAR(255)")
  private String firstName;

  @Column(name = "last_name", columnDefinition = "VARCHAR(255)")
  private String lastName;

  @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(255) NOT NULL")
  private String username;

  @Column(name = "password", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
  private String password;

  @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255) NOT NULL")
  private String email;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
