package com.globant.project.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.globant.project.domain.entities.ClientEntity;
import com.globant.project.domain.entities.OrderEntity;
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.repositories.OrderRepository;
import com.globant.project.services.ClientService;
import com.globant.project.services.OrderService;
import com.globant.project.services.ProductService;

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
    private final BigDecimal tax = new BigDecimal("0.19");

    @Override
    public OrderEntity createOrder(OrderEntity orderEntity) {

        ClientEntity client = clientService.getClient(orderEntity.getClientDocument().getDocument());
        ProductEntity product = productService.getProduct(orderEntity.getProductUuid().getUuid());
        orderEntity.setClientDocument(client);
        orderEntity.setProductUuid(product);
        orderEntity.setSubTotal(
                product.getPrice().multiply(new BigDecimal(orderEntity.getQuantity()).setScale(2, RoundingMode.FLOOR)));
        orderEntity.setTax(orderEntity.getSubTotal().multiply(tax).setScale(2, RoundingMode.FLOOR));
        orderEntity.setGrandTotal(orderEntity.getSubTotal().add(orderEntity.getTax()).setScale(2, RoundingMode.FLOOR));
        orderEntity.setDelivered(false);

        return orderRepository.save(orderEntity);

    }

    @Override
    public void updateOrder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateOrder'");
    }

    @Override
    public void deleteOrder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOrder'");
    }

    @Override
    public void getOrder() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrder'");
    }

    @Override
    public List<OrderEntity> getOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

}
