package com.globant.project.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globant.project.domain.entities.OrderEntity;

/**
 * DeliveryRepository
 */
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

}
