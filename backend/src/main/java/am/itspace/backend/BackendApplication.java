package am.itspace.backend;

import am.itspace.backend.entity.User;
import am.itspace.backend.entity.enums.Role;
import am.itspace.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class BackendApplication {

  private final PasswordEncoder passwordEncoder;

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }

  @Bean
  public CommandLineRunner run(UserRepository userRepository) {
    return args -> {

      String adminEmail = "admin@gmail.com";
      boolean isAdminExists = userRepository.existsByEmail(adminEmail);

      String adminPassword = "admin123";

      if (!isAdminExists) {
        User user = User.builder()
            .firstName("admin")
            .email(adminEmail)
            .password(passwordEncoder.encode(adminPassword))
            .role(Role.ADMIN)
            .build();
        userRepository.save(user);
        log.info("Admin has been registered successfully");
      } else {
        log.info("Admin already exists");
      }
    };
  }

}
