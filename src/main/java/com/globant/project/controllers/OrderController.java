package com.globant.project.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.entities.OrderEntity;
import com.globant.project.mappers.OrderMapper;
import com.globant.project.services.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
        OrderDTO orderSaved = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderSaved);

    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<OrderDTO> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);

    }

    @GetMapping("/{uuid}")
    public ResponseEntity<OrderDTO> getOrder(
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") @PathVariable(name = "uuid") String uuid) {
        OrderDTO order = orderService.getOrder(uuid);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<HttpStatus> updateOrder(
            @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$") @PathVariable(name = "uuid") String uuid,
            @Valid @RequestBody OrderDTO orderDto) {

        orderService.updateOrder(uuid, orderDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
