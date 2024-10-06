package com.globant.project.application.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globant.project.application.mappers.ProductMapper;
import com.globant.project.application.ports.in.services.ProductService;
import com.globant.project.application.ports.out.repositories.ProductRepository;
import com.globant.project.application.utils.ErrorConstants;
import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

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
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductDTO createProduct(ProductDTO productDto) {
        String fantasyName = formatFantasyName(productDto.getFantasyName());
        checkIfProductExists(fantasyName);

        productDto.setFantasyName(fantasyName);
        ProductEntity product = productMapper.DtoToEntity(productDto);
        ProductEntity savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", product.getUuid());
        return productMapper.EntityToDto(savedProduct);
    }

    @Transactional
    @Override
    public void updateProduct(String uuid, ProductDTO productDto) {
        ProductEntity existingProduct = getProductEntity(UUID.fromString(uuid));
        String fantasyName = formatFantasyName(productDto.getFantasyName());
        productDto.setFantasyName(fantasyName);

        log.info("Compare existing product {} with new product {}", existingProduct, productDto);
        if (!productIsDifferent(existingProduct, productDto)) {
            throw new ConflictException(ErrorConstants.PRODUCT_NO_DIFFERENT_FIELD + " " + uuid.toString());
        }

        if (!existingProduct.getFantasyName().equals(fantasyName)) {
            checkIfProductExists(fantasyName);
        }
        productDto.setUuid(UUID.fromString(uuid));
        productRepository.save(productMapper.DtoToEntity(productDto));
        log.info("Product updated with ID: {}", uuid);
    }

    @Transactional
    @Override
    public void deleteProduct(String uuid) {
        ProductEntity product = productRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException(ErrorConstants.PRODUCT_NOT_FOUND + " " + uuid));
        productRepository.delete(product);
        log.info("Product deleted with ID: {}", uuid);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDTO getProduct(String uuid) {
        return productRepository.findById(UUID.fromString(uuid)).map(productMapper::EntityToDto)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.PRODUCT_NOT_FOUND + " " + uuid));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream().map(productMapper::EntityToDto).toList();
    }

    @Transactional(readOnly = true)
    public boolean productExistsByFantasyName(String fantasyName) {
        Optional<ProductEntity> product = productRepository.findByFantasyName(fantasyName);
        return product.isPresent();
    }

    private String formatFantasyName(String fantasyName) {
        return fantasyName != null ? fantasyName.toUpperCase() : null;
    }

    private void checkIfProductExists(String fantasyName) {
        if (productExistsByFantasyName(fantasyName)) {
            throw new ConflictException(ErrorConstants.PRODUCT_ALREADY_EXIST + " " + fantasyName);
        }
    }

    private Boolean productIsDifferent(ProductEntity productSaved, ProductDTO productDto) {
        return !productSaved.getFantasyName().equals(productDto.getFantasyName())
                || !productSaved.getCategory().equals(productDto.getCategory())
                || !productSaved.getPrice().equals(productDto.getPrice())
                || !productSaved.getDescription().equals(productDto.getDescription())
                || !productSaved.getAvailable().equals(productDto.getAvailable());
    }

    @Transactional(readOnly = true)
    @Override
    public ProductEntity getProductEntity(UUID uuid) {
        return productRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.PRODUCT_NOT_FOUND + " " + uuid));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> getProductsByFantasyName(String fantasyName) {
        return productRepository.findByFantasyNameContaining(fantasyName).stream().map(productMapper::EntityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<List<ProductEntity>> getProductsAvailablesByCategory() {
        return productRepository.findByAvailable(true).stream()
                .collect(Collectors.groupingBy(ProductEntity::getCategory)).values().stream()
                .collect(Collectors.toSet());
    }

}
