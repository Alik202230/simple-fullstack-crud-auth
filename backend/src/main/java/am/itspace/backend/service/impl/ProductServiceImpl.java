package am.itspace.backend.service.impl;

import am.itspace.backend.dto.AllProductResponse;
import am.itspace.backend.dto.ProductByIdResponse;
import am.itspace.backend.dto.SaveProductRequest;
import am.itspace.backend.dto.SaveProductResponse;
import am.itspace.backend.dto.UpdateProductRequest;
import am.itspace.backend.dto.UpdateProductResponse;
import am.itspace.backend.entity.Image;
import am.itspace.backend.entity.Product;
import am.itspace.backend.exception.ProductNotFoundException;
import am.itspace.backend.mapper.ProductConverter;
import am.itspace.backend.repository.ProductRepository;
import am.itspace.backend.service.ProductService;
import am.itspace.backend.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Value("${upload.image.path}")
  private String filePath;

  @Value("${base.image.url}")
  private String baseImageUrl;

  @Override
  public SaveProductResponse create(SaveProductRequest request) throws IOException {
    Product product = Product.builder()
        .name(request.getName())
        .description(request.getDescription())
        .price(request.getPrice())
        .quantity(request.getQuantity())
        .category(request.getCategory())
        .build();

    this.productRepository.save(product);

    try {
      List<Image> images = ImageUtil.generateImages(request.getImage(), filePath, product);
      product.setImages(images);
      productRepository.save(product);
    } catch (Exception e) {
      throw new FileNotFoundException("Image not found given path");
    }


    List<String> imageUrls = product.getImages().stream()
        .map(image -> image.getFileName())
        .toList();

    return SaveProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .category(product.getCategory())
        .image(imageUrls)
        .build();
  }

  @Override
  public Optional<ProductByIdResponse> getProductById(Long id) {
    Optional<Product> optionalProduct = this.productRepository.findById(id);

    if (optionalProduct.isEmpty()) throw new ProductNotFoundException("Product with id " + id + " not found");

    return optionalProduct.map(product -> ProductConverter.toProductByIdResponse(product));
  }

  @Override
  public List<AllProductResponse> getAllProducts() {
    List<Product> products = this.productRepository.findAll();

    if (products.isEmpty()) throw new ProductNotFoundException("There are no available products");

    return products.stream()
        .map(product -> ProductConverter.toAllProductResponse(product))
        .toList();
  }

  @Override
  public UpdateProductResponse updateProduct(UpdateProductRequest request, Long id) {
    Optional<Product> optionalProduct = this.productRepository.findById(id);

    if  (optionalProduct.isEmpty()) throw new ProductNotFoundException("Product with id " + id + " not found");

    Product product = optionalProduct.get();

    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setQuantity(request.getQuantity());

    this.productRepository.save(product);

    return UpdateProductResponse.builder()
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .build();
  }

  @Override
  public void deleteProduct(Long id) {
    Optional<Product> optionalProduct = this.productRepository.findById(id);

    if (optionalProduct.isEmpty()) throw new ProductNotFoundException("Product with id " + id + " not found");

    this.productRepository.deleteById(id);
  }

}
