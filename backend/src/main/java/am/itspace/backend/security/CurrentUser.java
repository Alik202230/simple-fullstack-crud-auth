package am.itspace.backend.security;

import am.itspace.backend.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

  private final User user;

  public CurrentUser(User user) {
    super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
    this.user = user;
  }

  public User getUser() {
    return user;
  }

}
