package am.itspace.backend.service.impl;

import am.itspace.backend.dto.CartItemResponse;
import am.itspace.backend.dto.CartRequest;
import am.itspace.backend.dto.CartResponse;
import am.itspace.backend.entity.Cart;
import am.itspace.backend.exception.CartNotFoundException;
import am.itspace.backend.mapper.CartItemConverter;
import am.itspace.backend.repository.CartRepository;
import am.itspace.backend.security.CurrentUser;
import am.itspace.backend.service.CartItemService;
import am.itspace.backend.service.CartService;
import am.itspace.backend.utils.CartUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemService cartItemService;

  @Override
  @Transactional
  public CartResponse addItemToCart(CurrentUser user, CartRequest request) {

    Cart cart = cartRepository.findByUserId(user.getUser().getId())
        .orElseGet(() -> cartRepository.save(Cart.builder()
            .user(user.getUser())
            .build()));

    request.getItems().forEach(item ->
        cartItemService.addOrUpdateCartItem(cart, item.getProductId(), item.getQuantity()));

    List<CartItemResponse> items = cart.getCartItems().stream()
        .map(item -> CartItemConverter.toCartItemResponse(item))
        .toList();


    BigDecimal total = CartUtil.calculateTotalPrice(items);

    return CartResponse.builder()
        .cartId(cart.getId())
        .userId(user.getUser().getId())
        .items(items)
        .totalPrice(total)
        .build();
  }

  @Override
  @Transactional
  public void removeItemFromCart(CurrentUser user, Long productId) {
    Optional<Cart> optionalCart = cartRepository.findByUserId(user.getUser().getId());

    if (optionalCart.isEmpty()) throw new CartNotFoundException("Cart not found");

    Cart cart = optionalCart.get();

    cartItemService.removeCartItem(productId, cart);
  }

  @Override
  public CartResponse getCartByUser(CurrentUser user) {
    Long userId = user.getUser().getId();
    Cart cart = cartRepository.findByUserIdWithItems(userId)
        .orElseThrow(() -> new CartNotFoundException("Your cart is empty. Please add items to your cart"));

    List<CartItemResponse> items = cart.getCartItems().stream()
        .map(item -> CartItemConverter.toCartItemResponse(item))
        .toList();

    BigDecimal total = CartUtil.calculateTotalPrice(items);

    return CartResponse.builder()
        .cartId(cart.getId())
        .userId(user.getUser().getId())
        .items(items)
        .totalPrice(total)
        .build();
  }
}
