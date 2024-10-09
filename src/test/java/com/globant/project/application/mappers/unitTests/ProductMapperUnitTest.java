package com.globant.project.application.mappers.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.globant.project.application.mappers.ProductMapper;
import com.globant.project.application.mappers.ProductMapperImpl;
import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.entities.ProductEntity;

/**
 * ProductMapperTest
 */
public class ProductMapperUnitTest {

    private final ProductMapper clientMapper = new ProductMapperImpl();

    @Test
    public void testDtoToEntity() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setUuid(UUID.randomUUID());
        productDTO.setFantasyName("Fantasy Name");
        productDTO.setDescription("Description");
        productDTO.setPrice(new BigDecimal("100.00"));
        productDTO.setAvailable(true);
        productDTO.setCreatedAt(LocalDateTime.now());
        productDTO.setUpdatedAt(LocalDateTime.now());

        ProductEntity productEntity = clientMapper.DtoToEntity(productDTO);

        assertEquals(productDTO.getUuid(), productEntity.getUuid());
        assertEquals(productDTO.getFantasyName(), productEntity.getFantasyName());
        assertEquals(productDTO.getDescription(), productEntity.getDescription());
        assertEquals(productDTO.getPrice(), productEntity.getPrice());
        assertEquals(productDTO.getAvailable(), productEntity.getAvailable());
        assertEquals(productDTO.getCreatedAt(), productEntity.getCreatedAt());
        assertEquals(productDTO.getUpdatedAt(), productEntity.getUpdatedAt());
    }

    @Test
    public void testEntityToDto() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setUuid(UUID.randomUUID());
        productEntity.setFantasyName("Fantasy Name");
        productEntity.setDescription("Description");
        productEntity.setPrice(new BigDecimal("100.00"));
        productEntity.setAvailable(true);
        productEntity.setCreatedAt(LocalDateTime.now());
        productEntity.setUpdatedAt(LocalDateTime.now());

        ProductDTO productDTO = clientMapper.EntityToDto(productEntity);

        assertEquals(productEntity.getUuid(), productDTO.getUuid());
        assertEquals(productEntity.getFantasyName(), productDTO.getFantasyName());
        assertEquals(productEntity.getDescription(), productDTO.getDescription());
        assertEquals(productEntity.getPrice(), productDTO.getPrice());
        assertEquals(productEntity.getAvailable(), productDTO.getAvailable());
        assertEquals(productEntity.getCreatedAt(), productDTO.getCreatedAt());
        assertEquals(productEntity.getUpdatedAt(), productDTO.getUpdatedAt());
    }

    @Test
    public void testEntityToDtoNull() {
        ProductEntity productEntity = new ProductEntity();

        ProductDTO productDTO = clientMapper.EntityToDto(productEntity);

        assertEquals(productEntity.getUuid(), productDTO.getUuid());
        assertEquals(productEntity.getFantasyName(), productDTO.getFantasyName());
        assertEquals(productEntity.getDescription(), productDTO.getDescription());
        assertEquals(productEntity.getPrice(), productDTO.getPrice());
        assertEquals(productEntity.getAvailable(), productDTO.getAvailable());
        assertEquals(productEntity.getCreatedAt(), productDTO.getCreatedAt());
        assertEquals(productEntity.getUpdatedAt(), productDTO.getUpdatedAt());
    }

    @Test
    public void testDtoToEntityNull() {
        ProductDTO productDTO = new ProductDTO();

        ProductEntity productEntity = clientMapper.DtoToEntity(productDTO);

        assertEquals(productDTO.getUuid(), productEntity.getUuid());
        assertEquals(productDTO.getFantasyName(), productEntity.getFantasyName());
        assertEquals(productDTO.getDescription(), productEntity.getDescription());
        assertEquals(productDTO.getPrice(), productEntity.getPrice());
        assertEquals(productDTO.getAvailable(), productEntity.getAvailable());
        assertEquals(productDTO.getCreatedAt(), productEntity.getCreatedAt());
        assertEquals(productDTO.getUpdatedAt(), productEntity.getUpdatedAt());
    }

    @Test
    public void testEntityToDtoNullValues() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setUuid(UUID.randomUUID());
        productEntity.setFantasyName("Fantasy Name");

        ProductDTO productDTO = clientMapper.EntityToDto(productEntity);

        assertEquals(productEntity.getUuid(), productDTO.getUuid());
        assertEquals(productEntity.getFantasyName(), productDTO.getFantasyName());
        assertEquals(productEntity.getDescription(), productDTO.getDescription());
        assertEquals(productEntity.getPrice(), productDTO.getPrice());
        assertEquals(productEntity.getAvailable(), productDTO.getAvailable());
        assertEquals(productEntity.getCreatedAt(), productDTO.getCreatedAt());
        assertEquals(productEntity.getUpdatedAt(), productDTO.getUpdatedAt());
    }

    @Test
    public void testDtoToEntityNullValues() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setUuid(UUID.randomUUID());
        productDTO.setFantasyName("Fantasy Name");

        ProductEntity productEntity = clientMapper.DtoToEntity(productDTO);

        assertEquals(productDTO.getUuid(), productEntity.getUuid());
        assertEquals(productDTO.getFantasyName(), productEntity.getFantasyName());
        assertEquals(productDTO.getDescription(), productEntity.getDescription());
        assertEquals(productDTO.getPrice(), productEntity.getPrice());
        assertEquals(productDTO.getAvailable(), productEntity.getAvailable());
        assertEquals(productDTO.getCreatedAt(), productEntity.getCreatedAt());
        assertEquals(productDTO.getUpdatedAt(), productEntity.getUpdatedAt());
    }

    @Test
    public void testNullEntityToDto() {
        ProductEntity productEntity = null;

        ProductDTO productDTO = clientMapper.EntityToDto(productEntity);

        assertEquals(productEntity, productDTO);
    }

    @Test
    public void testNullDtoToEntity() {
        ProductDTO productDTO = null;

        ProductEntity productEntity = clientMapper.DtoToEntity(productDTO);

        assertEquals(productDTO, productEntity);
    }

}
