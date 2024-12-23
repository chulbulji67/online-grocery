package com.grocery.store.product_service.controller;


import com.grocery.store.product_service.entity.Product;
import com.grocery.store.product_service.service.ProductService;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/products")
//@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);



    // Create or Update product
    @PostMapping
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product) {
        log.info("LOg is ");
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Get all products with pagination
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(productService.getProducts(page, size));
    }

    // Search products by name or description with pagination
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(@RequestParam String searchTerm, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(productService.searchProducts(searchTerm, page, size));
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<Product>> getProductsByProductCodes(@RequestParam List<String > productCodes){
       return new ResponseEntity<>(productService.findProductsByProductsName(productCodes), HttpStatus.OK);
    }
}
