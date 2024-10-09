package com.globant.project.application.services.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.globant.project.application.ports.in.services.ProductService;
import com.globant.project.application.ports.out.repositories.ProductRepository;
import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.entities.Category;
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

/**
 * ProductServiceIntegrationTest
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private ProductDTO productDTO;
    private ProductDTO productSaved;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        productDTO = new ProductDTO("FantasyName", Category.FISH, "Description", new BigDecimal("1.00"),
                true);

        productSaved = productService.createProduct(productDTO);
    }

    @Test
    void givenValidProduct_whenCreateProduct_thenProductIsCreated() {
        assertEquals(1, productRepository.count());
    }

    @Test
    void givenExistingProduct_whenCreateProduct_thenConflictExceptionIsThrown() {
        assertThrows(ConflictException.class, () -> productService.createProduct(productDTO));
    }

    @Test
    void givenExistingProduct_whenUpdateProduct_thenProductIsUpdated() {
        productDTO.setFantasyName("FantasyNameUpdated");
        productService.updateProduct(productSaved.getUuid().toString(), productDTO);
        ProductDTO productUpdated = productService.getProduct(productSaved.getUuid().toString());
        assertEquals(productDTO.getFantasyName(), productUpdated.getFantasyName());
    }

    @Test
    void givenExistingProductWithNoChanges_whenUpdateProduct_thenConflictExceptionIsThrown() {
        assertThrows(ConflictException.class,
                () -> productService.updateProduct(productSaved.getUuid().toString(), productSaved));
    }

    @Test
    void givenNotExistingProduct_whenUpdateProduct_thenConflictExceptionIsThrown() {
        assertThrows(ConflictException.class,
                () -> productService.updateProduct(productSaved.getUuid().toString(), productSaved));
    }

    @Test
    void givenExistingProduct_whenDeleteClient_thenClientIsDeleted() {
        productService.deleteProduct(productSaved.getUuid().toString());
        assertEquals(0, productRepository.count());
    }

    @Test
    void givenNotExistingProduct_whenDeleteClient_thenNotFoundExceptionIsThrown() {
        assertThrows(NotFoundException.class,
                () -> productService.deleteProduct(UUID.randomUUID().toString()));
    }

    @Test
    void givenExistingProduct_whenGetProduct_thenProductIsReturned() {
        ProductDTO productRetrieved = productService.getProduct(productSaved.getUuid().toString());
        assertEquals(productSaved.getFantasyName(), productRetrieved.getFantasyName());
    }

    @Test
    void givenNotExistingProduct_whenGetProduct_thenNotFoundExceptionIsThrown() {
        assertThrows(NotFoundException.class, () -> productService.getProduct(UUID.randomUUID().toString()));
    }

    @Test
    void givenExistingProducts_whenGetProducts_thenAllProductsAreReturned() {
        ProductDTO productDTO2 = new ProductDTO("FantasyName2", Category.FISH, "Description2", new BigDecimal("2.00"),
                true);
        productService.createProduct(productDTO2);

        assertEquals(2, productService.getProducts().size());
    }

    @Test
    void givenExistingProduct_whenGetByFantasyName_thenProductIsReturned() {
        List<ProductDTO> productsRetrieved = productService.getProductsByFantasyName(productSaved.getFantasyName());
        assertEquals(1, productsRetrieved.size());
    }

    @Test
    void givenExistingProduct_whenGetProductsAvailableByCategory_thenProductsAreReturned() {
        Set<List<ProductEntity>> productsRetrieved = productService.getProductsAvailablesByCategory();
        assertEquals(1, productsRetrieved.size());
    }

    @Test
    void givenNotExistingProduct_whenFormatFantasyName_thenFantasyNameIsFormatted() {
        String fantasyName = productService.formatFantasyName("FantasyName");
        assertEquals("FANTASYNAME", fantasyName);
    }
}
