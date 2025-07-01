package am.itspace.backend.utils;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class CosineSimilarityUtil {

  public static double calculateCosineSimilarity(Map<Long, Double> productRatings1, Map<Long, Double> productRatings2) {
    Set<Long> commonUser = new HashSet<>(productRatings1.keySet());
    commonUser.retainAll(productRatings2.keySet());

    if (commonUser.isEmpty()) return 0.0;

    double dotProduct = 0.0;
    double magnitude1 = 0.0;
    double magnitude2 = 0.0;

    for (Long userId : commonUser) {
      dotProduct += productRatings1.get(userId) * productRatings2.get(userId);
    }

    for (Double rating : productRatings1.values()) {
      magnitude1 += Math.pow(rating, 2);
    }

    for (Double rating : productRatings2.values()) {
      magnitude2 += Math.pow(rating, 2);
    }

    magnitude1 = Math.sqrt(magnitude1);
    magnitude2 = Math.sqrt(magnitude2);

    if (magnitude1 == 0 || magnitude2 == 0) return 0.0;


    return dotProduct / (magnitude1 * magnitude2);
  }

}
