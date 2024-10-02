package com.globant.project.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.dto.ProductSalesDTO;
import com.globant.project.domain.dto.SalesReportDTO;
import com.globant.project.domain.entities.ClientEntity;
import com.globant.project.domain.entities.OrderEntity;
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.error.ErrorConstants;
import com.globant.project.error.exceptions.NotFoundException;
import com.globant.project.mappers.OrderMapper;
import com.globant.project.repositories.OrderRepository;
import com.globant.project.services.ClientService;
import com.globant.project.services.OrderService;
import com.globant.project.services.ProductService;
import com.globant.project.utils.CalculationUtils;
import com.globant.project.utils.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OrderServiceImpl
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final ProductService productService;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDto) {

        OrderEntity orderEntity = orderMapper.DtoToEntity(orderDto);
        ClientEntity clientEntity = clientService.getClientEntity(orderEntity.getClientDocument().getDocument());
        ProductEntity productEntity = productService.getProductEntity(orderEntity.getProductUuid().getUuid());

        orderEntity.setClientDocument(clientEntity);
        orderEntity.setProductUuid(productEntity);
        orderEntity
                .setSubTotal(CalculationUtils.calculateSubTotal(productEntity.getPrice(), orderEntity.getQuantity()));
        orderEntity.setTax(CalculationUtils.calculateTax(orderEntity.getSubTotal()));
        orderEntity
                .setGrandTotal(CalculationUtils.calculateGrandTotal(orderEntity.getSubTotal(), orderEntity.getTax()));
        orderEntity.setDelivered(false);

        OrderEntity orderSaved = orderRepository.save(orderEntity);
        log.info("Order created with uuid: {}", orderSaved.getUuid());
        return orderMapper.EntityToDto(orderSaved);

    }

    @Transactional(readOnly = true)
    @Override
    public OrderDTO getOrder(String uuid) {
        UUID orderUuid = UUID.fromString(uuid);
        return orderRepository.findById(orderUuid).map(orderMapper::EntityToDto)
                .orElseThrow(() -> new NotFoundException(ErrorConstants.ORDER_NOT_FOUND + " " + orderUuid));
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDTO> getOrders() {
        return orderRepository.findAll().stream().map(orderMapper::EntityToDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean orderExists(String uuid) {
        return orderRepository.existsById(UUID.fromString(uuid));
    }

    @Transactional
    @Override
    public void updateOrder(String uuid, OrderDTO orderEntity) {
        orderEntity.setUuid(UUID.fromString(uuid));
        if (!orderExists(uuid)) {
            throw new NotFoundException(ErrorConstants.ORDER_NOT_FOUND + " " + uuid);
        }
        orderRepository.save(orderMapper.DtoToEntity(orderEntity));
        log.info("Order updated with uuid: {}", uuid);
    }

    @Transactional
    @Override
    public void deleteOrder(String uuid) {
        if (!orderExists(uuid.toString())) {
            throw new NotFoundException(ErrorConstants.ORDER_NOT_FOUND + " " + uuid);
        }
        orderRepository.deleteById(UUID.fromString(uuid));
    }

    @Transactional(readOnly = true)
    @Override
    public SalesReportDTO findProductSalesReportByDates(String startDate, String endDate) {
        LocalDateTime start = DateUtils.getDateFromString(startDate);
        LocalDateTime end = DateUtils.getDateFromString(endDate);

        List<List<Object>> productSalesReport = orderRepository.findProductSalesReportByDates(start, end);
        List<ProductSalesDTO> productSalesReportDto = productSalesReport.stream()
                .map(r -> new ProductSalesDTO((String) r.get(0), (Long) r.get(1), (BigDecimal) r.get(2)))
                .toList();

        List<List<Object>> leastSalesReport = orderRepository.findLeastSoldProductsByDates(start, end);
        List<ProductSalesDTO> leastSalesReportDto = leastSalesReport.stream()
                .map(r -> new ProductSalesDTO((String) r.get(0), (Long) r.get(1), (BigDecimal) r.get(2)))
                .toList();

        List<List<Object>> mostSalesReport = orderRepository.findMostSoldProductByDates(start, end);
        List<ProductSalesDTO> mostSalesReportDto = mostSalesReport.stream()
                .map(r -> new ProductSalesDTO((String) r.get(0), (Long) r.get(1), (BigDecimal) r.get(2)))
                .toList();

        SalesReportDTO salesReportDto = new SalesReportDTO(productSalesReportDto, leastSalesReportDto,
                mostSalesReportDto);
        log.info("Sales report generated between {} and {}", start.toString(), end.toString());
        return salesReportDto;
    }

}
