package com.globant.project.integrationTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.entities.Category;
import com.globant.project.error.exceptions.ConflictException;
import com.globant.project.error.exceptions.NotFoundException;
import com.globant.project.services.ProductService;

/**
 * ProductControlelrIntegrationTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper;
    private static final String BASE_URL_V1 = "/api/v1/products";
    private static final String BASE_URL_V2 = "/api/v2/products";

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    private ProductDTO createProductDTO() {
        return new ProductDTO("FantasyName", Category.FISH, "Description", new BigDecimal("1.00"), true);
    }

    @Test
    public void testCreateProduct_success() throws Exception {
        ProductDTO productDTO = createProductDTO();

        when(productService.createProduct(productDTO)).thenReturn(productDTO);

        mockMvc.perform(post(BASE_URL_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fantasyName").value(productDTO.getFantasyName()));
    }

    @Test
    public void testCreateProduct_conflict() throws Exception {
        ProductDTO productDto = createProductDTO();

        when(productService.createProduct(productDto)).thenThrow(new ConflictException(" "));
        mockMvc.perform(post(BASE_URL_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateProduct_badRequest() throws Exception {
        ProductDTO productDto = createProductDTO();
        productDto.setFantasyName(null);

        mockMvc.perform(post(BASE_URL_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetProducts_success() throws Exception {
        ProductDTO productDTO = createProductDTO();

        when(productService.getProducts()).thenReturn(List.of(productDTO));
        mockMvc.perform(get(BASE_URL_V1))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProductsByFantasyName_success() throws Exception {
        ProductDTO productDTO = createProductDTO();

        when(productService.getProductsByFantasyName("FantasyName")).thenReturn(List.of(productDTO));
        mockMvc.perform(get(BASE_URL_V2 + "?fantasyName=FantasyName"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProduct_success() throws Exception {
        ProductDTO productDTO = createProductDTO();
        String uuid = UUID.randomUUID().toString();

        when(productService.getProduct(uuid)).thenReturn(productDTO);
        mockMvc.perform(get(BASE_URL_V1 + "/" + uuid))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProduct_notFound() throws Exception {
        String uuid = UUID.randomUUID().toString();

        doThrow(NotFoundException.class).when(productService).getProduct(uuid);
        mockMvc.perform(get(BASE_URL_V1 + "/" + uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProduct_badRequest() throws Exception {
        String uuid = "invalid-uuid";

        mockMvc.perform(get(BASE_URL_V1 + "/" + uuid))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateProduct_success() throws Exception {
        ProductDTO productDTO = createProductDTO();
        String uuid = UUID.randomUUID().toString();

        doNothing().when(productService).updateProduct(uuid, productDTO);
        mockMvc.perform(put(BASE_URL_V1 + "/" + uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateProduct_notFound() throws Exception {
        ProductDTO productDTO = createProductDTO();
        String uuid = UUID.randomUUID().toString();

        doThrow(NotFoundException.class).when(productService).updateProduct(uuid, productDTO);
        mockMvc.perform(put(BASE_URL_V1 + "/" + uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateProduct_conflict() throws Exception {
        ProductDTO productDTO = createProductDTO();
        String uuid = UUID.randomUUID().toString();

        doThrow(ConflictException.class).when(productService).updateProduct(uuid, productDTO);
        mockMvc.perform(put(BASE_URL_V1 + "/" + uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateProduct_badRequest() throws Exception {
        ProductDTO productDTO = createProductDTO();
        String uuid = "invalid-uuid";

        mockMvc.perform(put(BASE_URL_V1 + "/" + uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteProduct_success() throws Exception {
        String uuid = UUID.randomUUID().toString();

        doNothing().when(productService).deleteProduct(uuid);
        mockMvc.perform(delete(BASE_URL_V1 + "/" + uuid))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProduct_notFound() throws Exception {
        String uuid = UUID.randomUUID().toString();

        doThrow(NotFoundException.class).when(productService).deleteProduct(uuid);
        mockMvc.perform(delete(BASE_URL_V1 + "/" + uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProduct_badRequest() throws Exception {
        String uuid = "invalid-uuid";

        mockMvc.perform(delete(BASE_URL_V1 + "/" + uuid))
                .andExpect(status().isBadRequest());
    }

}
