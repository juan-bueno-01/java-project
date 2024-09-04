package com.globant.project.services;

import java.util.List;

import com.globant.project.domain.entities.OrderEntity;

/**
 * OrderService
 */
public interface OrderService {

    OrderEntity createOrder(OrderEntity orderEntity);

    void updateOrder();

    void deleteOrder();

    void getOrder();

    List<OrderEntity> getOrders();

}
