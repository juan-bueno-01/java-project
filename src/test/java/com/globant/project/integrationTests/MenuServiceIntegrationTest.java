package com.globant.project.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.globant.project.services.MenuService;

/**
 * MenuServiceIntegrationTest
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MenuServiceIntegrationTest {

    @Autowired
    private MenuService menuService;

    @Test
    public void testGetMenuPlainText() {
        String menu = menuService.getMenuPlainText();
        assertEquals(String.class, menu.getClass());
    }

    @Test
    public void testGetMenuPdf() throws IOException {
        byte[] menu = menuService.getMenuPdf();
        assertEquals(byte[].class, menu.getClass());
    }

}
