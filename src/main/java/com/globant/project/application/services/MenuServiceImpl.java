package com.globant.project.application.services;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globant.project.application.ports.in.services.MenuService;
import com.globant.project.application.ports.in.services.ProductService;
import com.globant.project.application.utils.MenuUtils;
import com.globant.project.domain.entities.ProductEntity;

import lombok.RequiredArgsConstructor;

/**
 * MenuServiceImpl
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final ProductService productService;

    @Transactional(readOnly = true)
    @Override
    public String getMenuPlainText() {
        Set<List<ProductEntity>> productsByCategory = productService.getProductsAvailablesByCategory();
        return buildPlainTextMenu(productsByCategory);
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] getMenuPdf() throws IOException {
        Set<List<ProductEntity>> productsByCategory = productService.getProductsAvailablesByCategory();
        return MenuUtils.formatMenuPdf(productsByCategory);
    }

    private String buildPlainTextMenu(Set<List<ProductEntity>> productsByCategory) {
        String categoriesMenu = productsByCategory.stream()
                .map(this::formatCategoryMenu)
                .collect(Collectors.joining("\n\n"));
        return MenuUtils.getHeader() + "\n\n\n" + categoriesMenu;
    }

    private String formatCategoryMenu(List<ProductEntity> products) {
        String categoryName = MenuUtils.formatCategory(products.get(0).getCategory().toString());
        String productsMenu = products.stream()
                .map(MenuUtils::formatProductMenu)
                .collect(Collectors.joining("\n"));
        return categoryName + "\n" + productsMenu;
    }
}
