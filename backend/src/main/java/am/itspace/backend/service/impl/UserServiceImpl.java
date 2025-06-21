package am.itspace.backend.service.impl;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    String jwtToken = jwtTokenUtil.generateToken(user.getEmail());
    User savedUser = this.userRepository.save(user);

    saveUserToken(savedUser, jwtToken);

    UserAuthResponse.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .token(jwtToken)
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

    final String jwtToken = jwtTokenUtil.generateToken(user.getEmail());

    UserAuthResponse response = UserConverter.toAuthResponse(user);
    response.setToken(jwtToken);

    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return Optional.of(response);

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

  public void saveUserToken(User user, String jwtToken) {
    Token token = Token.builder()
        .user(user)
        .token(jwtToken)
        .type(TokenType.BEARER)
        .isExpired(false)
        .revoked(false)
        .build();

    this.tokenRepository.save(token);
  }

}

