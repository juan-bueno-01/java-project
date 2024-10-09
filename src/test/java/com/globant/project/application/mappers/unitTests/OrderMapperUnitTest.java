package com.globant.project.application.mappers.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.globant.project.application.mappers.OrderMapper;
import com.globant.project.application.mappers.OrderMapperImpl;
import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.entities.ClientEntity;
import com.globant.project.domain.entities.OrderEntity;
import com.globant.project.domain.entities.ProductEntity;

/**
 * OrderMapperUnitTest
 */
public class OrderMapperUnitTest {

    private final OrderMapper orderMapper = new OrderMapperImpl();

    @Test
    public void testDtoToEntity() {
        OrderDTO orderDto = new OrderDTO();
        orderDto.setUuid(UUID.randomUUID());
        orderDto.setClientDocument("123456789");
        orderDto.setProductUuid(UUID.randomUUID().toString());
        orderDto.setQuantity(1);
        orderDto.setExtraInformation("Extra information");
        orderDto.setSubTotal(new BigDecimal("100.00"));
        orderDto.setTax(new BigDecimal("10.00"));
        orderDto.setGrandTotal(new BigDecimal("110.00"));
        orderDto.setDelivered(false);
        orderDto.setDeliveredDate(LocalDateTime.now());
        orderDto.setCreatedAt(LocalDateTime.now());
        orderDto.setUpdatedAt(LocalDateTime.now());

        OrderEntity orderEntity = orderMapper.DtoToEntity(orderDto);

        assertEquals(orderDto.getUuid(), orderEntity.getUuid());
        assertEquals(orderDto.getClientDocument(), orderEntity.getClientDocument().getDocument());
        assertEquals(orderDto.getProductUuid(), orderEntity.getProductUuid().getUuid().toString());
        assertEquals(orderDto.getQuantity(), orderEntity.getQuantity());
        assertEquals(orderDto.getExtraInformation(), orderEntity.getExtraInformation());
        assertEquals(orderDto.getSubTotal(), orderEntity.getSubTotal());
        assertEquals(orderDto.getTax(), orderEntity.getTax());
        assertEquals(orderDto.getGrandTotal(), orderEntity.getGrandTotal());
        assertEquals(orderDto.getDelivered(), orderEntity.getDelivered());
        assertEquals(orderDto.getDeliveredDate(), orderEntity.getDeliveredDate());
        assertEquals(orderDto.getCreatedAt(), orderEntity.getCreatedAt());
        assertEquals(orderDto.getUpdatedAt(), orderEntity.getUpdatedAt());
    }

    @Test
    public void testEntityToDto() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setDocument("CC-123456");

        ProductEntity productEntity = new ProductEntity();
        productEntity.setUuid(UUID.randomUUID());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUuid(UUID.randomUUID());
        orderEntity.setClientDocument(clientEntity);
        orderEntity.setProductUuid(productEntity);
        orderEntity.setQuantity(1);
        orderEntity.setExtraInformation("Extra information");
        orderEntity.setSubTotal(new BigDecimal("100.00"));
        orderEntity.setTax(new BigDecimal("10.00"));
        orderEntity.setGrandTotal(new BigDecimal("110.00"));
        orderEntity.setDelivered(false);
        orderEntity.setDeliveredDate(LocalDateTime.now());
        orderEntity.setCreatedAt(LocalDateTime.now());

        OrderDTO orderDto = orderMapper.EntityToDto(orderEntity);

        assertEquals(orderEntity.getUuid(), orderDto.getUuid());
        assertEquals(orderEntity.getClientDocument().getDocument(), orderDto.getClientDocument());
        assertEquals(orderEntity.getProductUuid().getUuid().toString(), orderDto.getProductUuid());
        assertEquals(orderEntity.getQuantity(), orderDto.getQuantity());
        assertEquals(orderEntity.getExtraInformation(), orderDto.getExtraInformation());
        assertEquals(orderEntity.getSubTotal(), orderDto.getSubTotal());
        assertEquals(orderEntity.getTax(), orderDto.getTax());
        assertEquals(orderEntity.getGrandTotal(), orderDto.getGrandTotal());
        assertEquals(orderEntity.getDelivered(), orderDto.getDelivered());
        assertEquals(orderEntity.getDeliveredDate(), orderDto.getDeliveredDate());
        assertEquals(orderEntity.getCreatedAt(), orderDto.getCreatedAt());
        assertEquals(orderEntity.getUpdatedAt(), orderDto.getUpdatedAt());
    }

    @Test
    public void testEntityToDtoNull() {
        OrderEntity orderEntity = new OrderEntity();

        OrderDTO orderDto = orderMapper.EntityToDto(orderEntity);

        assertEquals(orderEntity.getUuid(), orderDto.getUuid());
        assertEquals(orderEntity.getClientDocument(), orderDto.getClientDocument());
        assertEquals(orderEntity.getProductUuid(), orderDto.getProductUuid());
        assertEquals(orderEntity.getQuantity(), orderDto.getQuantity());
        assertEquals(orderEntity.getExtraInformation(), orderDto.getExtraInformation());
        assertEquals(orderEntity.getSubTotal(), orderDto.getSubTotal());
        assertEquals(orderEntity.getTax(), orderDto.getTax());
        assertEquals(orderEntity.getGrandTotal(), orderDto.getGrandTotal());
        assertEquals(orderEntity.getDelivered(), orderDto.getDelivered());
        assertEquals(orderEntity.getDeliveredDate(), orderDto.getDeliveredDate());
        assertEquals(orderEntity.getCreatedAt(), orderDto.getCreatedAt());
        assertEquals(orderEntity.getUpdatedAt(), orderDto.getUpdatedAt());
    }

    @Test
    public void testDtoToEntityNull() {
        OrderDTO orderDto = new OrderDTO();

        OrderEntity orderEntity = orderMapper.DtoToEntity(orderDto);

        assertEquals(orderDto.getUuid(), orderEntity.getUuid());
        assertEquals(orderDto.getClientDocument(), orderEntity.getClientDocument().getDocument());
        assertEquals(orderDto.getProductUuid(), orderEntity.getProductUuid().getUuid());
        assertEquals(orderDto.getQuantity(), orderEntity.getQuantity());
        assertEquals(orderDto.getExtraInformation(), orderEntity.getExtraInformation());
        assertEquals(orderDto.getSubTotal(), orderEntity.getSubTotal());
        assertEquals(orderDto.getTax(), orderEntity.getTax());
        assertEquals(orderDto.getGrandTotal(), orderEntity.getGrandTotal());
        assertEquals(orderDto.getDelivered(), orderEntity.getDelivered());
        assertEquals(orderDto.getDeliveredDate(), orderEntity.getDeliveredDate());
        assertEquals(orderDto.getCreatedAt(), orderEntity.getCreatedAt());
        assertEquals(orderDto.getUpdatedAt(), orderEntity.getUpdatedAt());
    }

    @Test
    public void testEntityToDtoNullValues() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setDocument("CC-123456");

        ProductEntity productEntity = new ProductEntity();
        productEntity.setUuid(UUID.randomUUID());

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setClientDocument(clientEntity);
        orderEntity.setProductUuid(productEntity);

        OrderDTO orderDto = orderMapper.EntityToDto(orderEntity);

        assertEquals(orderEntity.getUuid(), orderDto.getUuid());
        assertEquals(orderEntity.getClientDocument().getDocument(), orderDto.getClientDocument());
        assertEquals(orderEntity.getProductUuid().getUuid().toString(), orderDto.getProductUuid());
        assertEquals(orderEntity.getQuantity(), orderDto.getQuantity());
        assertEquals(orderEntity.getExtraInformation(), orderDto.getExtraInformation());
        assertEquals(orderEntity.getSubTotal(), orderDto.getSubTotal());
        assertEquals(orderEntity.getTax(), orderDto.getTax());
        assertEquals(orderEntity.getGrandTotal(), orderDto.getGrandTotal());
        assertEquals(orderEntity.getDelivered(), orderDto.getDelivered());
        assertEquals(orderEntity.getDeliveredDate(), orderDto.getDeliveredDate());
        assertEquals(orderEntity.getCreatedAt(), orderDto.getCreatedAt());
        assertEquals(orderEntity.getUpdatedAt(), orderDto.getUpdatedAt());
    }

    @Test
    public void testDtoToEntityNullValues() {
        OrderDTO orderDto = new OrderDTO();

        OrderEntity orderEntity = orderMapper.DtoToEntity(orderDto);

        assertEquals(orderDto.getUuid(), orderEntity.getUuid());
        assertEquals(orderDto.getClientDocument(), orderEntity.getClientDocument().getDocument());
        assertEquals(orderDto.getProductUuid(), orderEntity.getProductUuid().getUuid());
        assertEquals(orderDto.getQuantity(), orderEntity.getQuantity());
        assertEquals(orderDto.getExtraInformation(), orderEntity.getExtraInformation());
        assertEquals(orderDto.getSubTotal(), orderEntity.getSubTotal());
        assertEquals(orderDto.getTax(), orderEntity.getTax());
        assertEquals(orderDto.getGrandTotal(), orderEntity.getGrandTotal());
        assertEquals(orderDto.getDelivered(), orderEntity.getDelivered());
        assertEquals(orderDto.getDeliveredDate(), orderEntity.getDeliveredDate());
        assertEquals(orderDto.getCreatedAt(), orderEntity.getCreatedAt());
        assertEquals(orderDto.getUpdatedAt(), orderEntity.getUpdatedAt());
    }

    @Test
    public void testNullEntityToDto() {
        OrderDTO orderDto = orderMapper.EntityToDto(null);

        assertEquals(null, orderDto);
    }

    @Test
    public void testNullDtoToEntity() {
        OrderEntity orderEntity = orderMapper.DtoToEntity(null);

        assertEquals(null, orderEntity);
    }

}
