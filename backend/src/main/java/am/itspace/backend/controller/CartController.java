package am.itspace.backend.controller;

import am.itspace.backend.dto.CartRequest;
import am.itspace.backend.dto.CartResponse;
import am.itspace.backend.security.CurrentUser;
import am.itspace.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;

  @PostMapping("/add")
  public ResponseEntity<CartResponse> addItem(@RequestBody CartRequest request, @AuthenticationPrincipal CurrentUser user) {
    CartResponse response = cartService.addItemToCart(user, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/remove/{id}")
  public ResponseEntity<String> removeItem(@PathVariable("id") Long productId, @AuthenticationPrincipal CurrentUser user) {
    cartService.removeItemFromCart(user, productId);
    return ResponseEntity.ok("Item with id " + productId + " has been removed successfully");
  }

  @GetMapping("/me")
  public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal CurrentUser user) {
    CartResponse response = cartService.getCartByUser(user);
    return ResponseEntity.ok(response);
  }

}
