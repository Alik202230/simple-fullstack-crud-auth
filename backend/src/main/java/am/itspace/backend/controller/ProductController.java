package am.itspace.backend.controller;

import am.itspace.backend.dto.AllProductResponse;
import am.itspace.backend.dto.ProductByIdResponse;
import am.itspace.backend.dto.SaveProductRequest;
import am.itspace.backend.dto.SaveProductResponse;
import am.itspace.backend.dto.UpdateProductRequest;
import am.itspace.backend.dto.UpdateProductResponse;
import am.itspace.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<SaveProductResponse> create(@ModelAttribute SaveProductRequest request) throws IOException {
    SaveProductResponse response = this.productService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/all")
  public ResponseEntity<List<AllProductResponse>> getAllProducts() {
    List<AllProductResponse> response = this.productService.getAllProducts();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<ProductByIdResponse>> getProductById(@PathVariable Long id) {
    Optional<ProductByIdResponse> response = this.productService.getProductById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<UpdateProductResponse> updateProduct(
      @RequestBody UpdateProductRequest request, @PathVariable Long id
  ) {
    UpdateProductResponse response = this.productService.updateProduct(request, id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
    this.productService.deleteProduct(id);
    final String message = "Product with id " + id + " has been deleted successfully";
    return ResponseEntity.status(HttpStatus.OK).body(message);
  }

}
