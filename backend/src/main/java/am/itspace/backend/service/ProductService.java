package am.itspace.backend.service;

import am.itspace.backend.dto.AllProductResponse;
import am.itspace.backend.dto.ProductByIdResponse;
import am.itspace.backend.dto.SaveProductRequest;
import am.itspace.backend.dto.SaveProductResponse;
import am.itspace.backend.dto.UpdateProductRequest;
import am.itspace.backend.dto.UpdateProductResponse;
import am.itspace.backend.entity.Product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

  SaveProductResponse create(SaveProductRequest request) throws IOException;

  Optional<ProductByIdResponse> getProductById(Long id);

  List<AllProductResponse> getAllProducts();

  UpdateProductResponse updateProduct(UpdateProductRequest request, Long id);

  void deleteProduct(Long id);

}
