package com.globant.project.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globant.project.domain.entities.ProductEntity;

/**
 * ProductRepository
 */
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByFantasyName(String fantasyName);

}