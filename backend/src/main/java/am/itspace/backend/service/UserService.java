package am.itspace.backend.service;

import am.itspace.backend.dto.SaveUserRequest;
import am.itspace.backend.dto.UserAuthRequest;
import am.itspace.backend.dto.UserAuthResponse;

import java.util.Optional;

public interface UserService {

  void register(SaveUserRequest request);

  Optional<UserAuthResponse> login(UserAuthRequest request);

}
