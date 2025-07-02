package am.itspace.backend.repository;

import am.itspace.backend.entity.Cart;
import am.itspace.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

  @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci WHERE c.user.id = :userId")
  Optional<Cart> findByUserIdWithItems(@Param("userId") Long userId);

  Optional<Cart> findByUserId(Long userId);

  List<Cart> user(User user);
}
