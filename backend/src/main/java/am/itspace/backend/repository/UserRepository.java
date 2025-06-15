package am.itspace.backend.repository;

import am.itspace.backend.entity.User;
import am.itspace.backend.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Optional<User> findByRole(Role role);

  boolean existsByEmail(String adminEmail);
}
