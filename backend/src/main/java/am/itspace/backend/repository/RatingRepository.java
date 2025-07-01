package am.itspace.backend.repository;

import am.itspace.backend.entity.Product;
import am.itspace.backend.entity.Rating;
import am.itspace.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

  List<Rating> findByUser(User user);
  List<Rating> findByProduct(Product product);
//  Optional<Rating> findByUserAndProduct(User user, Product product);
//  List<Rating> findByUserId(Long userId);

}
