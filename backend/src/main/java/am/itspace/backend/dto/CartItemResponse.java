package am.itspace.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

  private Long productId;
  private String productName;
  private Integer quantity;
  private BigDecimal price;
  private String image;
}
