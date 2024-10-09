package com.globant.project.infrastructure.adapters.in.rest.unitTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.project.application.ports.in.services.ClientService;
import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;
import com.globant.project.infrastructure.adapters.in.rest.ClientController;
import com.globant.project.infrastructure.error.ErrorCode;

@WebMvcTest(ClientController.class)
public class ClientControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ErrorCode errorCode;

    private ObjectMapper objectMapper;

    private static final String BASE_URL_V1 = "/api/v1/clients";
    private static final String BASE_URL_V2 = "/api/v2/clients";
    private static final String DOCUMENT = "CC-123456";

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateClient_success() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        when(clientService.createClient(clientDTO)).thenReturn(clientDTO);

        mockMvc.perform(post(BASE_URL_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.document").value(clientDTO.getDocument()));
    }

    @Test
    public void testCreateClient_conflict() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        when(clientService.createClient(clientDTO)).thenThrow(new ConflictException("Document already exists"));

        mockMvc.perform(post(BASE_URL_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateClient_invalidData() throws Exception {
        ClientDTO clientDTO = new ClientDTO(null, "Juan Perez", null, "3123456789", "Calle 123");

        mockMvc.perform(post(BASE_URL_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetClient_success() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        when(clientService.getClient(DOCUMENT)).thenReturn(clientDTO);

        mockMvc.perform(get(BASE_URL_V1 + "/" + DOCUMENT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.document").value(clientDTO.getDocument()));
    }

    @Test
    public void testGetClient_notFound() throws Exception {
        when(clientService.getClient(DOCUMENT)).thenThrow(new NotFoundException("Client not found"));

        mockMvc.perform(get(BASE_URL_V1 + "/" + DOCUMENT))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetClients_success() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        when(clientService.getClients(Optional.empty(), Optional.empty())).thenReturn(List.of(clientDTO));

        mockMvc.perform(get(BASE_URL_V2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].document").value(clientDTO.getDocument()));
    }

    @Test
    public void testGetClientsWithParams_success() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        when(clientService.getClients(Optional.of("document"), Optional.of("asc"))).thenReturn(List.of(clientDTO));

        mockMvc.perform(get(BASE_URL_V2 + "?orderBy=document&direction=asc"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateClient_success() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        doNothing().when(clientService).updateClient(DOCUMENT, clientDTO);

        mockMvc.perform(put(BASE_URL_V1 + "/" + DOCUMENT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateClient_badRequest() throws Exception {
        ClientDTO clientDTO = createClientDTO();

        mockMvc.perform(put(BASE_URL_V1 + "/notValidDocument")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteClient_success() throws Exception {
        doNothing().when(clientService).deleteClient(DOCUMENT);

        mockMvc.perform(delete(BASE_URL_V1 + "/" + DOCUMENT))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteClient_notFound() throws Exception {
        doThrow(new NotFoundException("Client not found")).when(clientService).deleteClient(DOCUMENT);

        mockMvc.perform(delete(BASE_URL_V1 + "/" + DOCUMENT))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteClient_badRequest() throws Exception {
        mockMvc.perform(delete(BASE_URL_V1 + "/notValidDocument"))
                .andExpect(status().isBadRequest());
    }

    private ClientDTO createClientDTO() {
        return new ClientDTO(DOCUMENT, "Juan Perez", "juanPerez@example.com", "3123456789", "Calle 123");
    }
}
