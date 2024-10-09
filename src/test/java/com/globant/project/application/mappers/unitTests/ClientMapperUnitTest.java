package com.globant.project.application.mappers.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.globant.project.application.mappers.ClientMapper;
import com.globant.project.application.mappers.ClientMapperImpl;
import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.entities.ClientEntity;

/**
 * clientMapperUnitTest
 */
public class ClientMapperUnitTest {

    private final ClientMapper clientMapper = new ClientMapperImpl();

    @Test
    public void testDtoToEntity() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setDocument("CC-123456");
        clientDTO.setName("Juan Perez");
        clientDTO.setEmail("juanPerez@example.com");
        clientDTO.setPhone("3123456789");
        clientDTO.setDeliveryAddress("Calle 123");
        clientDTO.setCreatedAt(LocalDateTime.now());
        clientDTO.setUpdatedAt(LocalDateTime.now());

        ClientEntity clientEntity = clientMapper.DtoToEntity(clientDTO);

        assertEquals(clientDTO.getDocument(), clientEntity.getDocument());
        assertEquals(clientDTO.getName(), clientEntity.getName());
        assertEquals(clientDTO.getEmail(), clientEntity.getEmail());
        assertEquals(clientDTO.getPhone(), clientEntity.getPhone());
        assertEquals(clientDTO.getDeliveryAddress(), clientEntity.getDeliveryAddress());
        assertEquals(clientDTO.getCreatedAt(), clientEntity.getCreatedAt());
        assertEquals(clientDTO.getUpdatedAt(), clientEntity.getUpdatedAt());
    }

    @Test
    public void testEntityToDto() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setDocument("CC-123456");
        clientEntity.setName("Juan Perez");
        clientEntity.setEmail("juanPerez@example.com");
        clientEntity.setPhone("3123456789");
        clientEntity.setDeliveryAddress("Calle 123");
        clientEntity.setCreatedAt(LocalDateTime.now());
        clientEntity.setUpdatedAt(LocalDateTime.now());

        ClientDTO clientDto = clientMapper.EntityToDto(clientEntity);

        assertEquals(clientEntity.getDocument(), clientDto.getDocument());
        assertEquals(clientEntity.getName(), clientDto.getName());
        assertEquals(clientEntity.getEmail(), clientDto.getEmail());
        assertEquals(clientEntity.getPhone(), clientDto.getPhone());
        assertEquals(clientEntity.getDeliveryAddress(), clientDto.getDeliveryAddress());
        assertEquals(clientEntity.getCreatedAt(), clientDto.getCreatedAt());
        assertEquals(clientEntity.getUpdatedAt(), clientDto.getUpdatedAt());
    }

    @Test
    public void testEntityToDtoNull() {
        ClientEntity clientEntity = new ClientEntity();

        ClientDTO clientDto = clientMapper.EntityToDto(clientEntity);

        assertEquals(clientEntity.getDocument(), clientDto.getDocument());
        assertEquals(clientEntity.getName(), clientDto.getName());
        assertEquals(clientEntity.getEmail(), clientDto.getEmail());
        assertEquals(clientEntity.getPhone(), clientDto.getPhone());
        assertEquals(clientEntity.getDeliveryAddress(), clientDto.getDeliveryAddress());
        assertEquals(clientEntity.getCreatedAt(), clientDto.getCreatedAt());
        assertEquals(clientEntity.getUpdatedAt(), clientDto.getUpdatedAt());
    }

    @Test
    public void testDtoToEntityNull() {
        ClientDTO clientDto = new ClientDTO();

        ClientEntity clientEntity = clientMapper.DtoToEntity(clientDto);

        assertEquals(clientDto.getDocument(), clientEntity.getDocument());
        assertEquals(clientDto.getName(), clientEntity.getName());
        assertEquals(clientDto.getEmail(), clientEntity.getEmail());
        assertEquals(clientDto.getPhone(), clientEntity.getPhone());
        assertEquals(clientDto.getDeliveryAddress(), clientEntity.getDeliveryAddress());
        assertEquals(clientDto.getCreatedAt(), clientEntity.getCreatedAt());
        assertEquals(clientDto.getUpdatedAt(), clientEntity.getUpdatedAt());
    }

    @Test
    public void testEntityToDtoNullValues() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setDocument("CC-123456");

        ClientDTO clientDto = clientMapper.EntityToDto(clientEntity);

        assertEquals(clientEntity.getDocument(), clientDto.getDocument());
        assertEquals(clientEntity.getName(), clientDto.getName());
        assertEquals(clientEntity.getEmail(), clientDto.getEmail());
        assertEquals(clientEntity.getPhone(), clientDto.getPhone());
        assertEquals(clientEntity.getDeliveryAddress(), clientDto.getDeliveryAddress());
        assertEquals(clientEntity.getCreatedAt(), clientDto.getCreatedAt());
        assertEquals(clientEntity.getUpdatedAt(), clientDto.getUpdatedAt());
    }

    @Test
    public void testDtoToEntityNullValues() {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setDocument("CC-123456");

        ClientEntity clientEntity = clientMapper.DtoToEntity(clientDto);

        assertEquals(clientDto.getDocument(), clientEntity.getDocument());
        assertEquals(clientDto.getName(), clientEntity.getName());
        assertEquals(clientDto.getEmail(), clientEntity.getEmail());
        assertEquals(clientDto.getPhone(), clientEntity.getPhone());
        assertEquals(clientDto.getDeliveryAddress(), clientEntity.getDeliveryAddress());
        assertEquals(clientDto.getCreatedAt(), clientEntity.getCreatedAt());
        assertEquals(clientDto.getUpdatedAt(), clientEntity.getUpdatedAt());
    }

    @Test
    public void testNullEntityToDto() {
        ClientEntity clientEntity = null;

        ClientDTO clientDto = clientMapper.EntityToDto(clientEntity);

        assertEquals(clientEntity, clientDto);
    }

    @Test
    public void testNullDtoToEntity() {
        ClientDTO clientDto = null;

        ClientEntity clientEntity = clientMapper.DtoToEntity(clientDto);

        assertEquals(clientDto, clientEntity);
    }

}
