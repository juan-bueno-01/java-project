package com.globant.project.application.services.unitTests;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

import org.mockito.junit.jupiter.MockitoExtension;

import com.globant.project.application.mappers.OrderMapper;
import com.globant.project.application.ports.in.services.ClientService;
import com.globant.project.application.ports.in.services.ProductService;
import com.globant.project.application.ports.out.repositories.OrderRepository;
import com.globant.project.application.services.OrderServiceImpl;
import com.globant.project.application.utils.CalculationUtils;
import com.globant.project.application.utils.DateUtils;
import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.dto.ProductSalesDTO;
import com.globant.project.domain.dto.SalesReportDTO;
import com.globant.project.domain.entities.Category;
import com.globant.project.domain.entities.ClientEntity;
import com.globant.project.domain.entities.OrderEntity;
import com.globant.project.domain.entities.ProductEntity;
import com.globant.project.domain.excepions.NotFoundException;

/**
 * OrderServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ClientService clientService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderDTO orderDto;
    private OrderEntity orderEntity;

    private ClientDTO clientDto;
    private ClientEntity clientEntity;

    private ProductDTO productDto;
    private ProductEntity productEntity;

    @BeforeEach
    public void setup() {

        clientDto = new ClientDTO("CC-123456", "Juan Perez", "juanPerez@example.com",
                "3123456789", "Calle 123", LocalDateTime.now(), LocalDateTime.now());
        clientEntity = new ClientEntity("CC-123456", "Juan Perez",
                "juanPerez@example.com", "3123456789", "Calle 123", LocalDateTime.now(), LocalDateTime.now());

        productDto = new ProductDTO(new UUID(1, 1), "FANTASYNAME", Category.FISH, "Description", new BigDecimal(1),
                true, LocalDateTime.now(), LocalDateTime.now());
        productEntity = new ProductEntity(new UUID(1, 1), "FANTASYNAME", Category.FISH, "Description",
                new BigDecimal(1),
                true, LocalDateTime.now(), LocalDateTime.now());

        orderDto = new OrderDTO(new UUID(1, 1), clientDto.getDocument(), productDto.getUuid().toString(), 3,
                "Extra information");
        orderEntity = new OrderEntity(new UUID(1, 1), clientEntity, productEntity, 3, "Extra information");

        lenient().when(orderMapper.DtoToEntity(orderDto)).thenReturn(orderEntity);
        lenient().when(orderMapper.EntityToDto(orderEntity)).thenReturn(orderDto);
    }

    @Test
    public void createOrder_WhenProductExistsAndClientExists_ShouldCreateOrder() {
        orderEntity
                .setSubTotal(CalculationUtils.calculateSubTotal(productEntity.getPrice(), orderEntity.getQuantity()));
        orderEntity.setTax(CalculationUtils.calculateTax(orderEntity.getSubTotal()));
        orderEntity
                .setGrandTotal(CalculationUtils.calculateGrandTotal(orderEntity.getSubTotal(), orderEntity.getTax()));
        orderEntity.setDelivered(false);

        when(clientService.getClientEntity(orderEntity.getClientDocument().getDocument())).thenReturn(clientEntity);
        when(productService.getProductEntity(orderEntity.getProductUuid().getUuid())).thenReturn(productEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        OrderDTO result = orderService.createOrder(orderDto);

        orderDto.setSubTotal(CalculationUtils.calculateSubTotal(productEntity.getPrice(), orderDto.getQuantity()));
        orderDto.setTax(CalculationUtils.calculateTax(orderEntity.getSubTotal()));
        orderDto.setGrandTotal(CalculationUtils.calculateGrandTotal(orderDto.getSubTotal(), orderDto.getTax()));
        orderDto.setDelivered(false);

        assertEquals(orderDto, result);
    }

    @Test
    public void createOrder_WhenProductDoesNotExist_ShouldThrowNotFoundException() {
        when(clientService.getClientEntity(orderEntity.getClientDocument().getDocument())).thenReturn(clientEntity);
        when(productService.getProductEntity(orderEntity.getProductUuid().getUuid()))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> orderService.createOrder(orderDto));
    }

    @Test
    public void createOrder_WhenClientDoesNotExist_ShouldThrowNotFoundException() {
        when(clientService.getClientEntity(orderEntity.getClientDocument().getDocument()))
                .thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> orderService.createOrder(orderDto));
    }

    @Test
    public void getOrder_WhenOrderExists_ShouldReturnOrder() {
        when(orderRepository.findById(orderEntity.getUuid())).thenReturn(Optional.of(orderEntity));

        OrderDTO result = orderService.getOrder(orderEntity.getUuid().toString());

        assertEquals(orderDto, result);
    }

    @Test
    public void getOrder_WhenOrderDoesNotExist_ShouldThrowNotFoundException() {
        when(orderRepository.findById(orderEntity.getUuid())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.getOrder(orderEntity.getUuid().toString()));
    }

    @Test
    public void updateOrder_WhenOrderExists_ShouldUpdateOrder() {
        when(orderRepository.existsById(orderEntity.getUuid())).thenReturn(true);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        orderService.updateOrder(orderEntity.getUuid().toString(), orderDto);

        verify(orderRepository, times(1)).save(orderEntity);
    }

    @Test
    public void updateOrder_WhenOrderDoesNotExist_ShouldThrowNotFoundException() {
        when(orderRepository.existsById(orderEntity.getUuid())).thenReturn(false);
        assertThrows(NotFoundException.class,
                () -> orderService.updateOrder(orderEntity.getUuid().toString(), orderDto));

    }

    @Test
    public void deleteOrder_WhenOrderExists_ShouldDeleteOrder() {
        when(orderRepository.existsById(orderEntity.getUuid())).thenReturn(true);

        orderService.deleteOrder(orderEntity.getUuid().toString());

        verify(orderRepository, times(1)).deleteById(orderEntity.getUuid());
    }

    @Test
    public void deleteOrder_WhenOrderDoesNotExist_ShouldThrowNotFoundException() {
        when(orderRepository.existsById(orderEntity.getUuid())).thenReturn(false);
        assertThrows(NotFoundException.class,
                () -> orderService.deleteOrder(orderEntity.getUuid().toString()));
    }

    @Test
    public void orderExists_WhenOrderExists_ShouldReturnTrue() {
        when(orderRepository.existsById(orderEntity.getUuid())).thenReturn(true);

        boolean result = orderService.orderExists(orderEntity.getUuid().toString());

        assertEquals(true, result);
    }

    @Test
    public void getOrders_ShouldReturnOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(orderEntity));

        OrderDTO result = orderService.getOrders().get(0);

        assertEquals(orderDto, result);
    }

    @Test
    public void getSalesReportByDates_ShouldReturnSalesReport() {

        String startDate = "20230101";
        String endDate = "20250101";

        LocalDateTime start = DateUtils.getDateFromString(startDate);
        LocalDateTime end = DateUtils.getDateFromString(endDate);

        when(orderRepository.findProductSalesReportByDates(start, end))
                .thenReturn(List.of(List.of("FANTASYNAME", Long.valueOf(3), new BigDecimal(3))));

        when(orderRepository.findLeastSoldProductsByDates(start, end))
                .thenReturn(List.of(List.of("FANTASYNAME", Long.valueOf(3), new BigDecimal(3))));

        when(orderRepository.findMostSoldProductByDates(start, end))
                .thenReturn(List.of(List.of("FANTASYNAME", Long.valueOf(3), new BigDecimal(3))));

        ProductSalesDTO productSalesDto = new ProductSalesDTO("FANTASYNAME", Long.valueOf(3), new BigDecimal(3));

        List<ProductSalesDTO> leastSalesReportDto = List.of(productSalesDto);
        List<ProductSalesDTO> mostSalesReportDto = List.of(productSalesDto);

        SalesReportDTO salesReportDto = new SalesReportDTO(List.of(productSalesDto), leastSalesReportDto,
                mostSalesReportDto);

        SalesReportDTO result = orderService.findProductSalesReportByDates(startDate, endDate);

        assertEquals(salesReportDto, result);

    }
}
