package com.globant.project.controllers;

import java.util.List;

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
import com.globant.project.domain.dto.SalesReportDTO;
import com.globant.project.services.OrderService;
import com.globant.project.utils.RegexUtils;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

/**
 * OrderController
 */
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal server error"),
})
public class OrderController {

    private final OrderService orderService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid or incomplete order data"),
    })
    @PostMapping("v1/orders")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDto) {
        OrderDTO orderSaved = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderSaved);

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order list"),
    })
    @GetMapping("v1/orders")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<OrderDTO> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Order UUID is not valid"),
    })
    @GetMapping("v1/orders/{uuid}")
    public ResponseEntity<OrderDTO> getOrder(
            @Pattern(regexp = RegexUtils.UUID_REGEX) @PathVariable(name = "uuid") String uuid) {
        OrderDTO order = orderService.getOrder(uuid);
        return ResponseEntity.ok(order);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order updated"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Order UUID is not valid"),
    })
    @PutMapping("v1/orders/{uuid}")
    public ResponseEntity<Void> updateOrder(
            @Pattern(regexp = RegexUtils.UUID_REGEX) @PathVariable(name = "uuid") String uuid,
            @Valid @RequestBody OrderDTO orderDto) {
        orderService.updateOrder(uuid, orderDto);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report retrieved"),
            @ApiResponse(responseCode = "400", description = "Dates are not valid"),
    })
    @GetMapping("v2/orders/report/{startDate}/{endDate}")
    public ResponseEntity<SalesReportDTO> findProductSalesReportByDates(
            @Pattern(regexp = RegexUtils.DATE_REGEX) @PathVariable(name = "startDate") String startDate,
            @Pattern(regexp = RegexUtils.DATE_REGEX) @PathVariable(name = "endDate") String endDate) {
        SalesReportDTO report = orderService.findProductSalesReportByDates(startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
