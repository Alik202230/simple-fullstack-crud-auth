package am.itspace.backend.service;

import am.itspace.backend.entity.Cart;

public interface CartItemService {

  void addOrUpdateCartItem(Cart cart, Long productId, Integer quantity);

  void removeCartItem(Long id, Cart cart);

}
