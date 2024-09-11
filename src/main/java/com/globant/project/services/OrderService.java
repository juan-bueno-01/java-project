package com.globant.project.services;

import java.util.List;
import java.util.UUID;

import com.globant.project.domain.dto.OrderDTO;

/**
 * OrderService
 */
public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDto);

    void updateOrder(String UUID, OrderDTO orderDto);

    void deleteOrder(String UUID);

    OrderDTO getOrder(String uuid);

    List<OrderDTO> getOrders();

    boolean orderExists(String uuid);

}
