package com.globant.project.infrastructure.adapters.in.rest.integrationTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.project.application.ports.in.services.MenuService;

/**
 * MenuControllerIntegrationTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetMenuPlainText() throws Exception {
        when(menuService.getMenuPlainText()).thenReturn("Menu");
        mockMvc.perform(get("/api/v2/menu/plain"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain"));
    }

    @Test
    public void testGetMenuPdf() throws Exception {
        when(menuService.getMenuPdf()).thenReturn(new byte[0]);
        mockMvc.perform(get("/api/v2/menu/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"));

    }

}
