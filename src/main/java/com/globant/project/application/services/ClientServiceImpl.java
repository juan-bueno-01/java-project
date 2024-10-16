package com.globant.project.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globant.project.application.mappers.ClientMapper;
import com.globant.project.application.ports.in.services.ClientService;
import com.globant.project.application.ports.out.repositories.ClientRepository;
import com.globant.project.application.utils.ErrorConstants;
import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.entities.ClientEntity;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ClientServiceImpl
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    @Value("${default.client.orderBy}")
    private String orderBy;

    @Value("${default.client.direction}")
    private String direction;

    @Transactional
    @Override
    public ClientDTO createClient(ClientDTO clientDto) {

        String document = clientDto.getDocument();
        if (clientExists(document)) {
            throw new ConflictException(ErrorConstants.USER_ALREADY_EXIST + " " + document);
        }
        ClientEntity clientEntity = clientMapper.DtoToEntity(clientDto);
        ClientEntity savedClient = clientRepository.save(clientEntity);
        log.info("Client created with document: {}", document);
        savedClient.setCreatedAt(LocalDateTime.now());
        savedClient.setUpdatedAt(LocalDateTime.now());
        return clientMapper.EntityToDto(savedClient);
    }

    @Transactional
    @Override
    public void updateClient(String document, ClientDTO clientEntity) {
        if (!clientExists(document)) {
            throw new NotFoundException(ErrorConstants.USER_NOT_FOUND + " " + document);
        }
        clientRepository.save(clientMapper.DtoToEntity(clientEntity));
        log.info("Client updated with document: {}", document);
    }

    @Transactional
    @Override
    public void deleteClient(String document) {
        ClientEntity clientEntity = getClientEntity(document);
        clientRepository.delete(clientEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ClientDTO getClient(String document) {
        return clientMapper.EntityToDto(getClientEntity(document));
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean clientExists(String document) {
        return clientRepository.existsById(document);
    }

    @Override
    public ClientEntity getClientEntity(String document) {
        return clientRepository.findById(document)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.USER_NOT_FOUND + " " + document));
    }

    @Override
    public List<ClientDTO> getClients(Optional<String> orderBy, Optional<String> direction) {
        String sortDirection = direction.orElse(this.direction);
        if (sortDirection == null
                || (!sortDirection.equalsIgnoreCase("asc") && !sortDirection.equalsIgnoreCase("desc"))) {
            log.error("Invalid value for sortDirection: {}", sortDirection);
            throw new IllegalArgumentException("Invalid value '" + sortDirection
                    + "' for orders given; Has to be either 'desc' or 'asc' (case insensitive)");
        }
        Sort.Direction directionSort = Sort.Direction.fromString(direction.orElse(this.direction));
        String orderByValue = orderBy.orElse(this.orderBy);

        log.info("Clients ordered by {} with direction {}", orderByValue, directionSort);

        return clientRepository.findAll(Sort.by(directionSort, orderByValue)).stream().map(clientMapper::EntityToDto)
                .toList();
    }

}
