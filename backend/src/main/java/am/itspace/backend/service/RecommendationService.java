package am.itspace.backend.service;

import am.itspace.backend.entity.Product;

import java.util.List;

public interface RecommendationService {

  List<Product> getRecommendationsForProductBased(Long userId, int numRecommendations);

}
