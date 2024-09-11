package com.globant.project.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.services.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * ProductControllers
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDto) {
        ProductDTO productSaved = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<ProductDTO> getProduct(
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") @PathVariable(name = "uuid") String uuid) {
        ProductDTO product = productService.getProduct(uuid);
        return ResponseEntity.ok(product);

    }

    @PutMapping(path = "/{uuid}")
    public ResponseEntity<HttpStatus> updateProduct(
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") @PathVariable(name = "uuid") String uuid,
            @Valid @RequestBody ProductDTO productDto) {
        productService.updateProduct(uuid, productDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<HttpStatus> deleteProduct(
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") @PathVariable(name = "uuid") String uuid) {
        productService.deleteProduct(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
