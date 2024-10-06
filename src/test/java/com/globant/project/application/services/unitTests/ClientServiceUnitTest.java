package com.globant.project.application.services.unitTests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.lenient;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import com.globant.project.application.mappers.ClientMapper;
import com.globant.project.application.ports.out.repositories.ClientRepository;
import com.globant.project.application.services.ClientServiceImpl;
import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.entities.ClientEntity;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

/**
 * ClientServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class ClientServiceUnitTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    private ClientDTO clientDto;
    private ClientEntity clientEntity;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(clientService, "orderBy", "document");
        ReflectionTestUtils.setField(clientService, "direction", "desc");

        clientDto = new ClientDTO("CC-123456", "Juan Perez", "juanPerez@example.com",
                "3123456789", "Calle 123", LocalDateTime.now(), LocalDateTime.now());
        clientEntity = new ClientEntity("CC-123456", "Juan Perez",
                "juanPerez@example.com", "3123456789", "Calle 123", LocalDateTime.now(), LocalDateTime.now());

        lenient().when(clientMapper.DtoToEntity(clientDto)).thenReturn(clientEntity);
        lenient().when(clientMapper.EntityToDto(clientEntity)).thenReturn(clientDto);
    }

    @Test
    public void createClient_WhenClientDoesNotExist_ShouldCreateClient() {
        when(clientRepository.existsById(clientDto.getDocument())).thenReturn(false);
        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);

        ClientDTO result = clientService.createClient(clientDto);

        assertEquals(clientDto, result);
    }

    @Test
    public void createClient_WhenClientExists_ShouldThrowConflictException() {
        when(clientRepository.existsById(clientDto.getDocument())).thenReturn(true);

        assertThrows(ConflictException.class, () -> clientService.createClient(clientDto));
    }

    @Test
    public void updateClient_WhenClientExists_ShouldUpdateClient() {
        when(clientRepository.existsById(clientDto.getDocument())).thenReturn(true);
        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);

        clientService.updateClient(clientDto.getDocument(), clientDto);

        verify(clientRepository, times(1)).save(clientEntity);
    }

    @Test
    public void updateClient_WhenClientDoesNotExist_ShouldThrowNotFoundException() {
        when(clientRepository.existsById(clientDto.getDocument())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> clientService.updateClient(clientDto.getDocument(), clientDto));
    }

    @Test
    public void deleteClient_WhenClientExists_ShouldDeleteClient() {
        when(clientRepository.findById(clientDto.getDocument())).thenReturn(Optional.of(clientEntity));

        clientService.deleteClient(clientDto.getDocument());

        verify(clientRepository, times(1)).delete(clientEntity);
    }

    @Test
    public void deleteClient_WhenClientDoesNotExist_ShouldThrowNotFoundException() {
        when(clientRepository.findById(clientDto.getDocument())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.deleteClient(clientDto.getDocument()));
    }

    @Test
    public void getClient_WhenClientExists_ShouldReturnClient() {
        when(clientRepository.findById(clientDto.getDocument())).thenReturn(Optional.of(clientEntity));

        ClientDTO result = clientService.getClient(clientDto.getDocument());

        assertNotNull(result);
        assertEquals(clientDto, result);
    }

    @Test
    public void getClient_WhenClientDoesNotExist_ShouldThrowNotFoundException() {
        when(clientRepository.findById(clientDto.getDocument())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.getClient(clientDto.getDocument()));
    }

    @Test
    public void clientExists_WhenClientExists_ShouldReturnTrue() {
        when(clientRepository.existsById(clientDto.getDocument())).thenReturn(true);

        assertTrue(clientService.clientExists(clientDto.getDocument()));
    }

    @Test
    public void getClientsWithNoParams_ShouldReturnClientsSortedByDefault() {
        Sort sort = Sort.by(Sort.Direction.DESC, "document");
        when(clientRepository.findAll(sort)).thenReturn(List.of(clientEntity));

        assertEquals(List.of(clientDto), clientService.getClients(Optional.empty(), Optional.empty()));
    }

    @Test
    public void getClientsWithParams_ShouldReturnClientsSortedByParams() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        when(clientRepository.findAll(sort)).thenReturn(List.of(clientEntity));

        assertEquals(List.of(clientDto), clientService.getClients(Optional.of("name"), Optional.of("asc")));
    }

    @Test
    public void getClientEntity_WhenClientExists_ShouldReturnClientEntity() {
        when(clientRepository.findById(clientDto.getDocument())).thenReturn(Optional.of(clientEntity));

        assertEquals(clientEntity, clientService.getClientEntity(clientDto.getDocument()));
    }

    @Test
    public void getClientEntity_WhenClientDoesNotExist_ShouldThrowNotFoundException() {
        when(clientRepository.findById(clientDto.getDocument())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.getClientEntity(clientDto.getDocument()));
    }
}
