package com.globant.project.services;

import java.util.List;

import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.dto.SalesReportDTO;

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

    SalesReportDTO findProductSalesReportByDates(String startDate, String endDate);

}
