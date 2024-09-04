package com.globant.project.services;

import java.util.List;
import java.util.UUID;

import com.globant.project.domain.entities.ProductEntity;

/**
 * ProductServices
 */
public interface ProductService {

    ProductEntity createProduct(ProductEntity productEntity);

    void updateProduct(UUID uuid, ProductEntity productEntity);

    void deleteProduct(UUID uuid);

    ProductEntity getProduct(UUID uuid);

    List<ProductEntity> getProducts();

    boolean productExistsByFantasyName(String fantasyName);

    boolean productExistsByUUID(UUID uuid);

    boolean checkCategory(String category);
}
