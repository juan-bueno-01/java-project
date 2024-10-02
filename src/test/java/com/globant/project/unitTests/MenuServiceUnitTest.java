package com.globant.project.unitTests;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.globant.project.domain.entities.Category;
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.services.ProductService;
import com.globant.project.services.impl.MenuServiceImpl;

/**
 * MenuServiceUnitTest
 */
@ExtendWith(MockitoExtension.class)
public class MenuServiceUnitTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private MenuServiceImpl menuService;

    private Set<List<ProductEntity>> productsByCategory;

    @BeforeEach
    public void setup() {
        ProductEntity productEntity = new ProductEntity(new UUID(1, 1), "FANTASYNAME", Category.FISH, "Description",
                new BigDecimal(1),
                true, LocalDateTime.now(), LocalDateTime.now());

        productsByCategory = Set.of(List.of(productEntity));
    }

    @Test
    public void testGetMenuPlainText() {
        when(productService.getProductsAvailablesByCategory())
                .thenReturn(productsByCategory);
        menuService.getMenuPlainText();

        verify(productService, times(1)).getProductsAvailablesByCategory();
    }

    @Test
    public void testGetMenuPdf() throws IOException {
        when(productService.getProductsAvailablesByCategory())
                .thenReturn(productsByCategory);
        menuService.getMenuPdf();

        verify(productService, times(1)).getProductsAvailablesByCategory();
    }

}
