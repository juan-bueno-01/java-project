package com.globant.project.application.services.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.lenient;

import org.mockito.junit.jupiter.MockitoExtension;

import com.globant.project.application.mappers.ProductMapper;
import com.globant.project.application.ports.out.repositories.ProductRepository;
import com.globant.project.application.services.ProductServiceImpl;
import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.entities.Category;
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

/**
 * ProductServicesTests
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDTO productDto;
    private ProductEntity productEntity;

    @BeforeEach
    public void setup() {
        productDto = new ProductDTO(new UUID(1, 1), "FANTASYNAME", Category.FISH, "Description", new BigDecimal(1),
                true, LocalDateTime.now(), LocalDateTime.now());
        productEntity = new ProductEntity(new UUID(1, 1), "FANTASYNAME", Category.FISH, "Description",
                new BigDecimal(1),
                true, LocalDateTime.now(), LocalDateTime.now());
        lenient().when(productMapper.DtoToEntity(productDto)).thenReturn(productEntity);
        lenient().when(productMapper.EntityToDto(productEntity)).thenReturn(productDto);
    }

    @Test
    public void createProduct_WhenProductDoesNotExist_ShouldCreateProduct() {
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(productRepository.findByFantasyName(productDto.getFantasyName()))
                .thenReturn(Optional.empty());

        ProductDTO result = productService.createProduct(productDto);

        assertEquals(productDto, result);
    }

    @Test
    public void createProduct_WhenProductExists_ShouldThrowConflictException() {
        when(productRepository.findByFantasyName(productDto.getFantasyName()))
                .thenReturn(Optional.of(productEntity));

        assertThrows(ConflictException.class, () -> productService.createProduct(productDto));
    }

    @Test
    public void updateProduct_WhenProductDoesNotExist_ShouldThrowNotFoundException() {
        when(productRepository.findById(productDto.getUuid())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> productService.updateProduct(productDto.getUuid().toString(), productDto));
    }

    @Test
    public void updateProduct_WhenProductExists_ShouldUpdateProduct() {
        productDto.setFantasyName("NEWFANTASYNAME");
        productDto.setCategory(Category.CHICKEN);
        productDto.setDescription("NEWDESCRIPTION");
        productDto.setPrice(new BigDecimal(2));
        productDto.setAvailable(false);
        when(productRepository.findById(productDto.getUuid())).thenReturn(Optional.of(productEntity));
        when(productRepository.findByFantasyName(productDto.getFantasyName()))
                .thenReturn(Optional.empty());
        when(productRepository.save(productEntity)).thenReturn(productEntity);

        productService.updateProduct(productDto.getUuid().toString(), productDto);

        verify(productRepository, times(1)).findByFantasyName(productDto.getFantasyName());
        verify(productRepository, times(1)).findById(productDto.getUuid());
        verify(productRepository, times(1)).save(productEntity);
    }

    @Test
    public void updateProduct_WhenProductHasNoChanges_ShouldThrowConflictException() {
        when(productRepository.findById(productDto.getUuid())).thenReturn(Optional.of(productEntity));

        assertThrows(ConflictException.class,
                () -> productService.updateProduct(productDto.getUuid().toString(), productDto));
    }

    @Test
    public void deleteProduct_WhenProductExists_ShouldDeleteProduct() {
        when(productRepository.findById(productDto.getUuid())).thenReturn(Optional.of(productEntity));

        productService.deleteProduct(productDto.getUuid().toString());

        verify(productRepository, times(1)).delete(productEntity);
    }

    @Test
    public void deleteProduct_WhenProductDoesNotExist_ShouldThrowNotFoundException() {
        when(productRepository.findById(productDto.getUuid())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(productDto.getUuid().toString()));
    }

    @Test
    public void getProduct_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findById(productDto.getUuid())).thenReturn(Optional.of(productEntity));

        ProductDTO result = productService.getProduct(productDto.getUuid().toString());

        assertEquals(productDto, result);
    }

    @Test
    public void getProduct_WhenProductDoesNotExist_ShouldThrowNotFoundException() {
        when(productRepository.findById(productDto.getUuid())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProduct(productDto.getUuid().toString()));
    }

    @Test
    public void getProducts_WhenProductsExist_ShouldReturnProducts() {
        when(productRepository.findAll()).thenReturn(java.util.List.of(productEntity));

        assertEquals(java.util.List.of(productDto), productService.getProducts());
    }

    @Test
    public void productExistsByFantasyName_WhenProductExists_ShouldReturnTrue() {
        when(productRepository.findByFantasyName(productDto.getFantasyName())).thenReturn(Optional.of(productEntity));

        assertEquals(true, productService.productExistsByFantasyName(productDto.getFantasyName()));
    }

    @Test
    public void productExistsByFantasyName_WhenProductDoesNotExist_ShouldReturnFalse() {
        when(productRepository.findByFantasyName(productDto.getFantasyName())).thenReturn(Optional.empty());

        assertEquals(false, productService.productExistsByFantasyName(productDto.getFantasyName()));
    }

    @Test
    public void formatProduct_WhenProductExists_ShouldReturnProduct() {
        String fantasyName = "FantasyName";
        String formattedFantasyName = productService.formatFantasyName(fantasyName);
        assertEquals(fantasyName.toUpperCase(), formattedFantasyName);
    }

    @Test
    public void formatProduct_WhenProductDoesNotExist_ShouldReturnProduct() {
        String fantasyName = null;
        String formattedFantasyName = productService.formatFantasyName(fantasyName);
        assertEquals(null, formattedFantasyName);
    }

}
