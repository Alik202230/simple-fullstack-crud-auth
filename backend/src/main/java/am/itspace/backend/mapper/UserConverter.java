package am.itspace.backend.mapper;

import am.itspace.backend.dto.UserAuthResponse;
import am.itspace.backend.entity.User;

public class UserConverter {

  private UserConverter(){}

  public static UserAuthResponse toAuthResponse(User user) {
    return UserAuthResponse.builder()
        .userId(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .token(user.getEmail())
        .role(user.getRole())
        .build();
  }

}
