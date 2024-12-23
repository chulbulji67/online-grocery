package com.grocery.store.product_service.service;


import com.grocery.store.product_service.entity.Product;
import com.grocery.store.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create or Update a product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Get all products with pagination
    public Page<Product> getProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    // Search for products by name or description with pagination
    public Page<Product> searchProducts(String searchTerm, int page, int size) {
        return productRepository.searchByNameOrDescription(searchTerm, PageRequest.of(page, size));
    }

    public List<Product> findProductsByProductsName(List<String> productCodes){
        return productRepository.findByNameIn(productCodes);
    }
}

