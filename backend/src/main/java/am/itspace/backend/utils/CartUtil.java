package am.itspace.backend.utils;

import am.itspace.backend.dto.CartItemResponse;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class CartUtil {

  public static BigDecimal calculateTotalPrice(List<CartItemResponse> response) {
    return response.stream()
        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
