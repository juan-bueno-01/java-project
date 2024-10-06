package com.globant.project.application.services.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.globant.project.application.ports.in.services.ClientService;
import com.globant.project.application.ports.out.repositories.ClientRepository;
import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

/**
 * ClientServiceTest
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private ClientDTO clientDTO;
    private ClientDTO clientSaved;

    @BeforeEach
    void setup() {
        clientRepository.deleteAll();
        clientDTO = new ClientDTO("CC-123456", "Juan Perez", "juanPerez@example.com",
                "3123456789", "Calle 123");

        clientSaved = clientService.createClient(clientDTO);
    }

    @Test
    void givenValidClient_whenCreateClient_thenClientIsCreated() {
        assertEquals(clientDTO.getDocument(), clientSaved.getDocument());
    }

    @Test
    void givenExistingClient_whenCreateClient_thenConflictExceptionIsThrown() {
        assertThrows(ConflictException.class, () -> clientService.createClient(clientDTO));
    }

    @Test
    void givenExistingClient_whenUpdateClient_thenClientIsUpdated() {
        clientDTO.setDeliveryAddress("Calle 124");
        clientService.updateClient(clientSaved.getDocument(), clientDTO);
        ClientDTO clientUpdated = clientService.getClient(clientSaved.getDocument());
        assertEquals(clientDTO.getDeliveryAddress(), clientUpdated.getDeliveryAddress());
    }

    @Test
    void givenNotExistingClient_whenUpdateClient_thenNotFoundExceptionIsThrown() {
        clientDTO.setDocument("CC-123457");
        assertThrows(NotFoundException.class, () -> clientService.updateClient(clientDTO.getDocument(), clientDTO));
    }

    @Test
    void givenNotExistingClient_whenDeleteClient_thenNotFoundExceptionIsThrown() {
        clientDTO.setDocument("CC-123457");
        assertThrows(NotFoundException.class, () -> clientService.deleteClient(clientDTO.getDocument()));
    }

    @Test
    void givenExistingClient_whenDeleteClient_thenClientIsDeleted() {
        clientService.deleteClient(clientSaved.getDocument());
        assertEquals(0, clientRepository.count());
    }

    @Test
    void givenExistingClient_whenGetClient_thenClientIsReturned() {
        ClientDTO clientFound = clientService.getClient(clientSaved.getDocument());
        assertEquals(clientSaved.getDocument(), clientFound.getDocument());
    }

    @Test
    void givenNotExistingClient_whenGetClient_thenNotFoundExceptionIsThrown() {
        clientDTO.setDocument("CC-123457");
        assertThrows(NotFoundException.class, () -> clientService.getClient(clientDTO.getDocument()));
    }

    @Test
    void whenGetClients_thenAllClientsAreReturned() {
        List<ClientDTO> clients = clientService.getClients(Optional.empty(), Optional.empty());
        assertEquals(1, clients.size());
    }

    @Test
    void givenOrderByParam_whenGetClients_thenClientIsReturned() {
        List<ClientDTO> clients = clientService.getClients(Optional.of("document"), Optional.of("asc"));
        assertEquals(1, clients.size());
    }

}
