package com.globant.project.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.services.MenuService;
import com.globant.project.services.ProductService;
import com.globant.project.utils.MenuUtils;

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
        String menu = productsByCategory.stream()
                .map(products -> MenuUtils.formatCategory(products.get(0).getCategory().toString()) + "\n"
                        + products.stream().map(MenuUtils::formatProductMenu).collect(Collectors.joining("\n")))
                .collect(Collectors.joining("\n\n"));
        return MenuUtils.getHeader() + "\n\n\n" + menu;
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] getMenuPdf() throws IOException {
        Set<List<ProductEntity>> productsByCategory = productService.getProductsAvailablesByCategory();
        return MenuUtils.formatMenuPdf(productsByCategory);

    }

}
