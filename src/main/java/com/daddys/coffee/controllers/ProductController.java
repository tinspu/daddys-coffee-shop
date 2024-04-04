package com.daddys.coffee.controllers;

import com.daddys.coffee.entities.Product;
import com.daddys.coffee.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ComponentScan
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(@Autowired ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok((List<Product>) productRepository.findAll());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product newProduct) {
        Product savedProduct = productRepository.save(new Product(newProduct.getId(), newProduct.getName(), newProduct.getPrice()));
        return new ResponseEntity<Product>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product updatedProduct, @PathVariable long id) {
        Product matchingProduct = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No product has Id " + id));
        //TODO : update the code below
        matchingProduct.setName(updatedProduct.getName());
        matchingProduct.setPrice(updatedProduct.getPrice());
        productRepository.save(matchingProduct);
        return new ResponseEntity<>(matchingProduct, HttpStatus.OK);
    }

}
