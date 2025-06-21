package am.itspace.backend.mapper;

import am.itspace.backend.dto.CartItemResponse;
import am.itspace.backend.entity.CartItem;

public class CartItemConverter {

  private CartItemConverter(){}

  public static CartItemResponse toCartItemResponse(CartItem cartItem) {

    final String BASE_IMAGE_URL = "http://localhost:8080/images/";

    return CartItemResponse.builder()
        .productId(cartItem.getProduct().getId())
        .productName(cartItem.getProduct().getName())
        .quantity(cartItem.getQuantity())
        .price(cartItem.getProduct().getPrice())
        .image(BASE_IMAGE_URL + cartItem.getProduct().getImages().getFirst().getFileName())
        .build();
  }

}

