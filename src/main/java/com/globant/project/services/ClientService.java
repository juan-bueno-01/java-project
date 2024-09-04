package com.globant.project.services;

import java.util.List;

import com.globant.project.domain.entities.ClientEntity;

/**
 * ClientService
 */

public interface ClientService {

    ClientEntity createClient(ClientEntity clientEntity);

    Boolean clientExists(String document);

    void updateClient(String document, ClientEntity clientEntity);

    void deleteClient(String document);

    ClientEntity getClient(String document);

    List<ClientEntity> getClients();
}
