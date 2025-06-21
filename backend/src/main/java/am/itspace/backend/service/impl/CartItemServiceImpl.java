package am.itspace.backend.service.impl;

import am.itspace.backend.entity.Cart;
import am.itspace.backend.entity.CartItem;
import am.itspace.backend.entity.Product;
import am.itspace.backend.exception.ProductNotFoundException;
import am.itspace.backend.repository.CartItemRepository;
import am.itspace.backend.repository.ProductRepository;
import am.itspace.backend.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

  private final ProductRepository productRepository;
  private final CartItemRepository cartItemRepository;

  @Override
  public void addOrUpdateCartItem(Cart cart, Long productId, Integer quantity) {

    Optional<Product> optionalProduct = productRepository.findById(productId);

    if (optionalProduct.isEmpty()) throw new ProductNotFoundException("Product with id " + productId + " not found");

    Product product = optionalProduct.get();

    Optional<CartItem> optionalCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

    CartItem cartItem;

    if (optionalCartItem.isPresent()) {

      cartItem = optionalCartItem.get();

      cartItem.setQuantity(cartItem.getQuantity() + quantity);

    } else {
      cartItem = CartItem.builder()
          .cart(cart)
          .product(product)
          .quantity(quantity)
          .build();
      if (cart.getCartItems() == null) cart.setCartItems(new ArrayList<>());

      this.cartItemRepository.save(cartItem);
      cart.getCartItems().add(cartItem);
    }

  }

  @Override
  public void removeCartItem(Long productId, Cart cart) {
    cartItemRepository.removeProductFromCartByCartIdAndProductId(cart.getId(), productId);

//    cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
//        .ifPresent(cartItem -> {
//          cartItemRepository.delete(cartItem);
//          cart.getCartItems().remove(cartItem);
//        });
  }
}
