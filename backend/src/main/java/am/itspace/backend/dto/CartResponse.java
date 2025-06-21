package am.itspace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
  private Long cartId;
  private Long userId;
  private List<CartItemResponse> items;
  private BigDecimal totalPrice;
}
