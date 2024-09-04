package com.globant.project.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.entities.OrderEntity;
import com.globant.project.mappers.OrderMapper;
import com.globant.project.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * OrderController
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDto) {
        OrderEntity orderEntity = orderMapper.DtoToEntity(orderDto);
        OrderEntity orderSaved = orderService.createOrder(orderEntity);
        return new ResponseEntity<>(orderMapper.EntityToDto(orderSaved), HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<OrderEntity> orders = orderService.getOrders();
        return new ResponseEntity<>(orders.stream().map(orderMapper::EntityToDto).toList(), HttpStatus.OK);

    }

}
