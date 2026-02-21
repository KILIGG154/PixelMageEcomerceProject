package com.example.PixelMageEcomerceProject.service.interfaces;

import com.example.PixelMageEcomerceProject.dto.request.ProductRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    Product createProduct(ProductRequestDTO productRequestDTO);
    Product updateProduct(Integer id, ProductRequestDTO productRequestDTO);
    void deleteProduct(Integer id);
    Optional<Product> getProductById(Integer id);
    List<Product> getAllProducts();
    Optional<Product> getProductByName(String name);
}

