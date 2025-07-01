package am.itspace.backend.service;

import am.itspace.backend.dto.RefreshTokenResponse;
import am.itspace.backend.dto.SaveUserRequest;
import am.itspace.backend.dto.UserAuthRequest;
import am.itspace.backend.dto.UserAuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface UserService {

  void register(SaveUserRequest request);

  Optional<UserAuthResponse> login(UserAuthRequest request);

  RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
