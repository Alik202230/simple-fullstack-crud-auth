package am.itspace.backend.mapper;

import am.itspace.backend.dto.AllProductResponse;
import am.itspace.backend.dto.ProductByIdResponse;
import am.itspace.backend.dto.SaveProductResponse;
import am.itspace.backend.dto.UpdateProductResponse;
import am.itspace.backend.entity.Product;

import java.util.List;

public class ProductConverter {

  private ProductConverter() {
  }

  public static SaveProductResponse toSaveProductResponse(Product product) {

    List<String> images = product.getImages()
        .stream()
        .map(image -> image.getFileName())
        .toList();

    return SaveProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .category(product.getCategory())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .image(images)
        .build();
  }

  public static ProductByIdResponse toProductByIdResponse(Product product) {

    final String BASE_IMAGE_URL = "http://localhost:8080/images/";

    List<String> images = product.getImages()
        .stream()
        .map(image -> BASE_IMAGE_URL + image.getFileName())
        .toList();

    return ProductByIdResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .category(product.getCategory())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .images(images)
        .build();
  }

  public static AllProductResponse toAllProductResponse(Product product) {

    final String BASE_IMAGE_URL = "http://localhost:8080/images/";

    String imageUrl = null;
    if (product.getImages() != null && !product.getImages().isEmpty()) {
      imageUrl = BASE_IMAGE_URL + product.getImages().getFirst().getFileName();
    }

    return AllProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .image(imageUrl)
        .build();
  }

  public static UpdateProductResponse toUpdateProductResponse(Product product) {
    return UpdateProductResponse.builder()
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .build();
  }

}
