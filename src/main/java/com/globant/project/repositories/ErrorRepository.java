package com.globant.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globant.project.domain.entities.ErrorEntity;

/**
 * ErrorRepository
 */
public interface ErrorRepository extends JpaRepository<ErrorEntity, String> {

}
