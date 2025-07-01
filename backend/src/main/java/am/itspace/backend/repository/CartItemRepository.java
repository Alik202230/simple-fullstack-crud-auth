package am.itspace.backend.repository;

import am.itspace.backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  @Query(
      value = """
            SELECT *
            FROM cart_item_tbl
            WHERE cart_id = :cartId AND product_id = :productId
          """,
      nativeQuery = true
  )
  Optional<CartItem> findByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);

  @Modifying
  @Query("DELETE FROM CartItem ci WHERE ci.cart.Id = :cartId AND ci.product.id = :productId")
  void removeProductFromCartByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
}
