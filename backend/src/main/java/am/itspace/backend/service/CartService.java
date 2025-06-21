package am.itspace.backend.service;

import am.itspace.backend.dto.CartRequest;
import am.itspace.backend.dto.CartResponse;
import am.itspace.backend.security.CurrentUser;

public interface CartService {

  CartResponse addItemToCart(CurrentUser user, CartRequest request);
  void removeItemFromCart(CurrentUser user, Long productId);
  CartResponse getCartByUser(CurrentUser user);
}
