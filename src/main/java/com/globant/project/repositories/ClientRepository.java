package com.globant.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globant.project.domain.entities.ClientEntity;

/**
 * ClientRepository
 */
public interface ClientRepository extends JpaRepository<ClientEntity, String> {

}
