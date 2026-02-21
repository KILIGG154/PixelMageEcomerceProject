package com.example.PixelMageEcomerceProject.service.impl;

import com.example.PixelMageEcomerceProject.dto.request.ProductRequestDTO;
import com.example.PixelMageEcomerceProject.entity.Product;
import com.example.PixelMageEcomerceProject.repository.ProductRepository;
import com.example.PixelMageEcomerceProject.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setImageUrl(productRequestDTO.getImageUrl());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Integer id, ProductRequestDTO productRequestDTO) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setName(productRequestDTO.getName());
            updatedProduct.setDescription(productRequestDTO.getDescription());
            updatedProduct.setPrice(productRequestDTO.getPrice());
            updatedProduct.setImageUrl(productRequestDTO.getImageUrl());
            return productRepository.save(updatedProduct);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }
}
