package com.globant.project.integrationTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.project.domain.dto.ErrorDTO;
import com.globant.project.error.exceptions.ConflictException;
import com.globant.project.error.exceptions.NotFoundException;
import com.globant.project.services.ErrorService;

/**
 * ErrorControllerIntegrationTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ErrorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorService errorService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    private ErrorDTO createErrorDTO() {
        return new ErrorDTO("E4004", "Not found", "The requested resource was not found");
    }

    @Test
    public void testCreateError_success() throws Exception {
        ErrorDTO errorDTO = createErrorDTO();

        when(errorService.createError(errorDTO)).thenReturn(errorDTO);

        mockMvc.perform(post("/api/v2/errors")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(errorDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(errorDTO)));
    }

    @Test
    public void testCreateError_Conflict() throws Exception {
        ErrorDTO errorDTO = createErrorDTO();

        when(errorService.createError(errorDTO)).thenThrow(new ConflictException("Error already exists"));

        mockMvc.perform(post("/api/v2/errors")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(errorDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetErrors_success() throws Exception {
        ErrorDTO errorDTO = createErrorDTO();

        when(errorService.getErrors()).thenReturn(List.of(errorDTO));

        mockMvc.perform(get("/api/v2/errors"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(errorDTO))));
    }

    @Test
    public void testGetError_NotFound() throws Exception {
        when(errorService.getError("E4004")).thenThrow(new NotFoundException("Error not found"));

        mockMvc.perform(get("/api/v2/errors/E4004"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetError_success() throws Exception {
        ErrorDTO errorDTO = createErrorDTO();

        when(errorService.getError("E4004")).thenReturn(errorDTO);

        mockMvc.perform(get("/api/v2/errors/E4004"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(errorDTO)));
    }

    @Test
    public void testUpdateError_success() throws Exception {
        ErrorDTO errorDTO = createErrorDTO();

        mockMvc.perform(put("/api/v2/errors/E4004")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(errorDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateError_NotFound() throws Exception {
        ErrorDTO errorDTO = createErrorDTO();

        doThrow(new NotFoundException("Error not found")).when(errorService).updateError("E4004", errorDTO);

        mockMvc.perform(put("/api/v2/errors/E4004")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(errorDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteError_success() throws Exception {
        mockMvc.perform(delete("/api/v2/errors/E4004"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteError_NotFound() throws Exception {
        doThrow(new NotFoundException("Error not found")).when(errorService).deleteError("E4004");

        mockMvc.perform(delete("/api/v2/errors/E4004"))
                .andExpect(status().isNotFound());
    }

}
