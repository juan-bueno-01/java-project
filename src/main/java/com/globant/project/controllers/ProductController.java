package com.globant.project.controllers;

import java.util.List;
import java.util.UUID;

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
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.mappers.ProductMapper;
import com.globant.project.services.ProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ProductControllers
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDto) {
        ProductEntity productEntity = productMapper.DtoToEntity(productDto);
        ProductEntity productSaved = productService.createProduct(productEntity);
        return new ResponseEntity<>(productMapper.EntityToDto(productSaved), HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductEntity> products = productService.getProducts();
        return new ResponseEntity<>(products.stream().map(productMapper::EntityToDto).toList(), HttpStatus.OK);
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<ProductDTO> getProduct(
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") @PathVariable(name = "uuid") String uuid) {
        log.info("UUID: {}", uuid);
        ProductEntity productEntity = productService.getProduct(UUID.fromString(uuid));
        return new ResponseEntity<>(productMapper.EntityToDto(productEntity), HttpStatus.OK);

    }

    @PutMapping(path = "/{uuid}")
    public ResponseEntity<HttpStatus> updateProduct(
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") @PathVariable(name = "uuid") String uuid,
            @Valid @RequestBody ProductDTO productDto) {
        ProductEntity productEntity = productMapper.DtoToEntity(productDto);
        productService.updateProduct(UUID.fromString(uuid), productEntity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<HttpStatus> deleteProduct(
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") @PathVariable(name = "uuid") String uuid) {
        productService.deleteProduct(UUID.fromString(uuid));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
