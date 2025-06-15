package am.itspace.backend.dto;

import am.itspace.backend.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductByIdResponse {
  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer quantity;
  private Category category;
  private List<String> images;
}
