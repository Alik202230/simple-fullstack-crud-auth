package am.itspace.backend.controller;

import am.itspace.backend.dto.RefreshTokenResponse;
import am.itspace.backend.dto.SaveUserRequest;
import am.itspace.backend.dto.UserAuthRequest;
import am.itspace.backend.dto.UserAuthResponse;
import am.itspace.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<Void> register(@RequestBody @Valid SaveUserRequest saveUserRequest) {
    userService.register(saveUserRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<Optional<UserAuthResponse>> login(@RequestBody UserAuthRequest userAuthRequest) {
    Optional<UserAuthResponse> response = userService.login(userAuthRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/refresh-token")
  public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    return userService.refreshToken(request, response);
  }

}
