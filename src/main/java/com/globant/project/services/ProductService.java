package com.globant.project.services;

import java.util.List;
import java.util.UUID;

import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.entities.ProductEntity;

/**
 * ProductServices
 */
public interface ProductService {

    ProductDTO createProduct(ProductDTO productDto);

    void updateProduct(String uuid, ProductDTO productEntity);

    void deleteProduct(String uuid);

    ProductDTO getProduct(String uuid);

    List<ProductDTO> getProducts();

    ProductEntity getProductEntity(UUID uuid);

}
