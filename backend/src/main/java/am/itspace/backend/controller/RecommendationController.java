package am.itspace.backend.controller;

import am.itspace.backend.entity.Product;
import am.itspace.backend.security.CurrentUser;
import am.itspace.backend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendation")
public class RecommendationController {

  private final RecommendationService recommendationService;

  @GetMapping("/user-based")
  public ResponseEntity<List<Product>> getProductBasedRecommendations(@AuthenticationPrincipal CurrentUser user, @RequestParam(defaultValue = "10") Integer limit) {
    if (user == null) return ResponseEntity.status(401).build();
    Long userId = user.getUser().getId();
    List<Product> recommendations = recommendationService.getRecommendationsForProductBased(userId, limit);
    return ResponseEntity.ok(recommendations);
  }

}
