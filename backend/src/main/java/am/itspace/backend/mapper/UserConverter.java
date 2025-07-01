package am.itspace.backend.mapper;

import am.itspace.backend.dto.UserAuthResponse;
import am.itspace.backend.entity.User;
import org.springframework.http.HttpStatus;

public class UserConverter {

  private UserConverter(){}

  public static UserAuthResponse toAuthResponse(User user) {
    return UserAuthResponse.builder()
        .userId(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .accessToken(user.getEmail())
        .role(user.getRole())
        .statusCode(HttpStatus.OK.value())
        .build();
  }

}
