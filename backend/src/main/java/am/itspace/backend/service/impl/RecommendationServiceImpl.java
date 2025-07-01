package am.itspace.backend.service.impl;

import am.itspace.backend.entity.Product;
import am.itspace.backend.entity.Rating;
import am.itspace.backend.entity.User;
import am.itspace.backend.repository.ProductRepository;
import am.itspace.backend.repository.RatingRepository;
import am.itspace.backend.service.RecommendationService;
import am.itspace.backend.utils.CosineSimilarityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

  private final RatingRepository ratingRepository;
  private final ProductRepository productRepository;

  @Override
  public List<Product> getRecommendationsForProductBased(Long userId, int numRecommendations) {
    User user = ratingRepository.findById(userId).map(Rating::getUser).orElse(null);
    if (user == null) return Collections.emptyList();

    List<Rating> userRatings = this.ratingRepository.findByUser(user);
    Map<Long, Double> userRatedProductMap = userRatings.stream()
        .collect(Collectors.toMap(rating -> rating.getProduct().getId(), Rating::getScore));

    if (userRatedProductMap.isEmpty()) return Collections.emptyList();

    List<Product> products = this.productRepository.findAll();

    Map<Product, Double> productSimilarities = new HashMap<>();

    for (Map.Entry<Long, Double> entry : userRatedProductMap.entrySet()) {
      Long userRatedProductId = entry.getKey();
      Double userRatedProductScore = entry.getValue();

      Product ratedProduct = this.productRepository.findById(userRatedProductId).orElse(null);
      if (ratedProduct == null) continue;

      Map<Long, Double> ratingsForRatedProduct = ratingRepository.findByProduct(ratedProduct).stream()
          .collect(Collectors.toMap(rating -> rating.getUser().getId(), Rating::getScore));

      for (Product candidateProduct : products) {
        if (userRatedProductMap.containsKey(candidateProduct.getId()))  continue;

        Map<Long, Double> ratingsForCandidateProduct = ratingRepository.findByProduct(candidateProduct)
            .stream().collect(Collectors.toMap(rating -> rating.getUser().getId(), Rating::getScore));

        double similarity = CosineSimilarityUtil.calculateCosineSimilarity(ratingsForRatedProduct, ratingsForCandidateProduct);
        if (similarity > 0.0) productSimilarities.merge(candidateProduct, similarity * userRatedProductScore, Double::sum);
      }

    }

    return productSimilarities.entrySet().stream()
        .sorted(Map.Entry.<Product, Double>comparingByValue().reversed())
        .limit(numRecommendations)
        .map(Map.Entry::getKey)
        .toList();
  }

}
