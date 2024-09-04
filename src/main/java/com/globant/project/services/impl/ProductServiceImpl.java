package com.globant.project.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.globant.project.domain.entities.Category;
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.error.exceptions.ConflictException;
import com.globant.project.error.exceptions.NotFoundException;
import com.globant.project.repositories.ProductRepository;
import com.globant.project.services.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ProductServicesIpl
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductEntity createProduct(ProductEntity productEntity) {
        if (productExistsByFantasyName(productEntity.getFantasyName())) {
            throw new ConflictException("E2001 " + productEntity.getFantasyName());
        }

        productEntity.setFantasyName(productEntity.getFantasyName().toUpperCase());
        return productRepository.save(productEntity);
    }

    @Override
    public void updateProduct(UUID uuid, ProductEntity productEntity) {
        ProductEntity productSaved = getProduct(uuid);
        if (productSaved.equals(productEntity)) {
            throw new ConflictException("E2004 " + uuid.toString());
        }

        if (!productSaved.getFantasyName().equals(productEntity.getFantasyName())) {
            if (productExistsByFantasyName(productEntity.getFantasyName())) {
                throw new ConflictException("E2001 " + productEntity.getFantasyName());
            }

        }
        productRepository.save(productEntity);
    }

    @Override
    public void deleteProduct(UUID uuid) {
        if (!productExistsByUUID(uuid)) {
            throw new NotFoundException("E2003 " + uuid);
        }
        productRepository.deleteById(uuid);
    }

    @Override
    public ProductEntity getProduct(UUID uuid) {
        if (!productExistsByUUID(uuid)) {
            throw new NotFoundException("E2003 " + uuid);
        }
        return productRepository.findById(uuid).get();
    }

    @Override
    public List<ProductEntity> getProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public boolean productExistsByFantasyName(String fantasyName) {
        Optional<ProductEntity> product = productRepository.findByFantasyName(fantasyName);
        return product.isPresent();
    }

    @Override
    public boolean checkCategory(String category) {
        try {
            Category.valueOf(category);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean productExistsByUUID(UUID uuid) {
        Optional<ProductEntity> product = productRepository.findById(uuid);
        return product.isPresent();
    }

}
