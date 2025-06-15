package am.itspace.backend.dto;

import am.itspace.backend.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveProductRequest {
  private String name;
  private String description;
  private BigDecimal price;
  private Integer quantity;
  private Category category;
  private List<MultipartFile> image;
}
