package com.globant.project.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.globant.project.domain.entities.ClientEntity;
import com.globant.project.error.exceptions.ConflictException;
import com.globant.project.error.exceptions.NotFoundException;
import com.globant.project.repositories.ClientRepository;
import com.globant.project.services.ClientService;

import lombok.RequiredArgsConstructor;

/**
 * ClientServiceImpl
 */

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public ClientEntity createClient(ClientEntity clientEntity) {

        if (clientExists(clientEntity.getDocument())) {
            throw new ConflictException("E1001 " + clientEntity.getDocument());
        }
        return clientRepository.save(clientEntity);
    }

    @Override
    public void updateClient(String document, ClientEntity clientEntity) {
        if (!clientExists(document)) {
            throw new NotFoundException("E1002 " + clientEntity.getDocument());
        }
        clientRepository.save(clientEntity);
    }

    @Override
    public void deleteClient(String document) {
        if (!clientExists(document)) {
            throw new NotFoundException("E1002 " + document);
        }
        clientRepository.deleteById(document);
    }

    @Override
    public ClientEntity getClient(String document) {
        if (!clientExists(document)) {
            throw new NotFoundException("E1002 " + document);
        }
        return clientRepository.findById(document).get();
    }

    @Override
    public List<ClientEntity> getClients() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(),
                false).collect(Collectors.toList());
    }

    @Override
    public Boolean clientExists(String document) {
        return clientRepository.existsById(document);
    }

}
