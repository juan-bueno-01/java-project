package com.globant.project.infrastructure.adapters.in.rest.integrationTests;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globant.project.application.ports.in.services.OrderService;
import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.dto.ProductSalesDTO;
import com.globant.project.domain.dto.SalesReportDTO;
import com.globant.project.domain.excepions.ConflictException;
import com.globant.project.domain.excepions.NotFoundException;

/**
 * OrderControllerIntegrationTest
 */
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    private OrderDTO createOrderDTO() {
        return new OrderDTO(UUID.randomUUID(), "CC-123456", UUID.randomUUID().toString(), 1, "Extra information");
    }

    @Test
    public void testCreateOrder_success() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        when(orderService.createOrder(orderDTO)).thenReturn(orderDTO);

        mockMvc.perform(post("/api/v1/orders")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(orderDTO)));
    }

    @Test
    public void testCreateOrder_BadRequest() throws Exception {
        OrderDTO orderDTO = createOrderDTO();
        orderDTO.setUuid(null);
        orderDTO.setClientDocument("NotValidDocument");

        when(orderService.createOrder(orderDTO)).thenThrow(new ConflictException(" "));

        mockMvc.perform(post("/api/v1/orders")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateOrder_conflict() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        when(orderService.createOrder(orderDTO)).thenThrow(new ConflictException(" "));

        mockMvc.perform(post("/api/v1/orders")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testGetOrders_success() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        when(orderService.getOrders()).thenReturn(List.of(orderDTO));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(orderDTO))));
    }

    @Test
    public void testGetOrderById_success() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        when(orderService.getOrder(orderDTO.getUuid().toString())).thenReturn(orderDTO);

        mockMvc.perform(get("/api/v1/orders/" + orderDTO.getUuid().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orderDTO)));
    }

    @Test
    public void testGetOrderById_notFound() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        when(orderService.getOrder(orderDTO.getUuid().toString())).thenThrow(new NotFoundException(" "));

        mockMvc.perform(get("/api/v1/orders/" + orderDTO.getUuid().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateOrder_success() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        mockMvc.perform(put("/api/v1/orders/" + orderDTO.getUuid().toString())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateOrder_notFound() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        doThrow(new NotFoundException(" ")).when(orderService).updateOrder(orderDTO.getUuid().toString(), orderDTO);

        mockMvc.perform(put("/api/v1/orders/" + orderDTO.getUuid().toString())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateOrder_conflict() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        doThrow(new ConflictException(" ")).when(orderService).updateOrder(orderDTO.getUuid().toString(), orderDTO);

        mockMvc.perform(put("/api/v1/orders/" + orderDTO.getUuid().toString())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateOrder_badRequest() throws Exception {
        OrderDTO orderDTO = createOrderDTO();

        mockMvc.perform(put("/api/v1/orders/" + "InvalidUUID")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetSalesReport_success() throws Exception {
        SalesReportDTO salesReportDTO = new SalesReportDTO(List.of(new ProductSalesDTO()),
                List.of(new ProductSalesDTO()), List.of(new ProductSalesDTO()));
        String startDate = "20230101";
        String endDate = "20250101";

        when(orderService.findProductSalesReportByDates(startDate, endDate)).thenReturn(salesReportDTO);

        mockMvc.perform(get("/api/v2/orders/report")
                .param("startDate", startDate)
                .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(salesReportDTO)));
    }

}
