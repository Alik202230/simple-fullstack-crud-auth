package am.itspace.backend.service.impl;

import am.itspace.backend.dto.RefreshTokenResponse;
import am.itspace.backend.dto.SaveUserRequest;
import am.itspace.backend.dto.UserAuthRequest;
import am.itspace.backend.dto.UserAuthResponse;
import am.itspace.backend.entity.Token;
import am.itspace.backend.entity.User;
import am.itspace.backend.entity.enums.Role;
import am.itspace.backend.entity.enums.TokenType;
import am.itspace.backend.exception.AuthorizationException;
import am.itspace.backend.exception.UserAlreadyExistsException;
import am.itspace.backend.exception.UserNotFoundException;
import am.itspace.backend.mapper.UserConverter;
import am.itspace.backend.repository.TokenRepository;
import am.itspace.backend.repository.UserRepository;
import am.itspace.backend.service.UserService;
import am.itspace.backend.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final JwtTokenUtil jwtTokenUtil;
  private final PasswordEncoder passwordEncoder;
  private final TokenRepository tokenRepository;

  @Override
  public void register(SaveUserRequest request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent())
      throw new UserAlreadyExistsException("User with " + request.getEmail() + " already exists");

    final String encodedPassword = passwordEncoder.encode(request.getPassword());

    User user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(encodedPassword)
        .role(Role.USER)
        .build();

    final String accessToken = jwtTokenUtil.generateToken(user.getEmail());
    final String refreshToken = jwtTokenUtil.refreshToken(accessToken);
    User savedUser = this.userRepository.save(user);

    saveUserToken(savedUser, accessToken, refreshToken);

    UserAuthResponse.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }


  @Override
  public Optional<UserAuthResponse> login(UserAuthRequest request) {
    Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

    if (optionalUser.isEmpty())
      throw new UserNotFoundException("User with email " + request.getEmail() + " not found");

    User user = optionalUser.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
      throw new AuthorizationException("Wrong password or email");

    final String accessToken = jwtTokenUtil.generateToken(user.getEmail());
    final String refreshToken = jwtTokenUtil.refreshToken(accessToken);

    UserAuthResponse response = UserConverter.toAuthResponse(user);

    response.setAccessToken(accessToken);
    response.setRefreshToken(refreshToken);
    revokeAllUserTokens(user);
    saveUserToken(user, accessToken, refreshToken);

    return Optional.of(response);

  }

  @Override
  public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null && !header.startsWith("Bearer ")) {
      return null;
    }

    String token = header.substring(7);
    String username = jwtTokenUtil.getUsernameFromToken(token);

    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new UserNotFoundException("User with email " + username + " not found"));

    if (jwtTokenUtil.validateToken(token, username)) {

      final String accessToken = jwtTokenUtil.generateToken(user.getEmail());
      final String refreshToken = jwtTokenUtil.refreshToken(accessToken);

      revokeAllUserTokens(user);
      saveUserToken(user, accessToken, refreshToken);

      return RefreshTokenResponse.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .statusCode(HttpServletResponse.SC_OK)
          .build();
    }

    return null;
  }

  public void revokeAllUserTokens(User user) {
    List<Token> validUserTokens = this.tokenRepository.findAllValidTokensByUserId(user.getId());

    if (validUserTokens.isEmpty()) return;

    validUserTokens.forEach(token -> {
      token.setRevoked(true);
      token.setExpired(true);
    });

    this.tokenRepository.saveAll(validUserTokens);
  }

  public void saveUserToken(User user, String accessToken, String refreshToken) {
    Token token = Token.builder()
        .user(user)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .type(TokenType.BEARER)
        .isExpired(false)
        .revoked(false)
        .build();

    this.tokenRepository.save(token);
  }

}