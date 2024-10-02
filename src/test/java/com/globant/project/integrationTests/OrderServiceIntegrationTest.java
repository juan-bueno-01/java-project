package com.globant.project.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.globant.project.domain.dto.ClientDTO;
import com.globant.project.domain.dto.OrderDTO;
import com.globant.project.domain.dto.ProductDTO;
import com.globant.project.domain.dto.SalesReportDTO;
import com.globant.project.domain.entities.Category;
import com.globant.project.error.exceptions.NotFoundException;
import com.globant.project.repositories.ClientRepository;
import com.globant.project.repositories.OrderRepository;
import com.globant.project.repositories.ProductRepository;
import com.globant.project.services.ClientService;
import com.globant.project.services.OrderService;
import com.globant.project.services.ProductService;

/**
 * OrderServiceIntegrationTest
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    private OrderDTO orderDTO;
    private OrderDTO orderSaved;
    private ClientDTO clientDTO;
    private ClientDTO clientSaved;
    private ProductDTO productDTO;
    private ProductDTO productSaved;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        clientRepository.deleteAll();

        clientDTO = new ClientDTO("CC-123456", "Juan Perez", "juanPerez@example.com",
                "3123456789", "Calle 123", LocalDateTime.now(), LocalDateTime.now());
        clientSaved = clientService.createClient(clientDTO);

        productDTO = new ProductDTO("FANTASYNAME", Category.FISH, "Description", new BigDecimal(1),
                true);
        productSaved = productService.createProduct(productDTO);

        orderDTO = new OrderDTO(new UUID(1, 1), clientSaved.getDocument(), productSaved.getUuid().toString(), 3,
                "Extra information");
        orderSaved = orderService.createOrder(orderDTO);
    }

    @Test
    void givenValidOrder_whenCreateOrder_thenOrderIsCreated() {
        assert orderSaved.getClientDocument().equals(orderDTO.getClientDocument());
    }

    @Test
    void givenNotExistingClient_whenCreateOrder_thenNotFoundExceptionIsThrown() {
        orderDTO.setClientDocument("CC-654321");
        assertThrows(NotFoundException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    void givenNotExistingProduct_whenCreateOrder_thenNotFoundExceptionIsThrown() {
        orderDTO.setProductUuid(new UUID(2, 2).toString());
        assertThrows(NotFoundException.class, () -> orderService.createOrder(orderDTO));
    }

    @Test
    void givenExistingOrder_whenGetOrder_thenOrderIsReturned() {
        OrderDTO order = orderService.getOrder(orderSaved.getUuid().toString());
        assertEquals(orderSaved.getUuid(), order.getUuid());
    }

    @Test
    void givenNotExistingOrder_whenGetOrder_thenNotFoundExceptionIsThrown() {
        assertThrows(NotFoundException.class, () -> orderService.getOrder(new UUID(2, 2).toString()));
    }

    @Test
    void givenExistingOrder_whenDeleteOrder_thenOrderIsDeleted() {
        orderService.deleteOrder(orderSaved.getUuid().toString());
        assertEquals(0, orderRepository.count());
    }

    @Test
    void givenNotExistingOrder_whenDeleteOrder_thenNotFoundExceptionIsThrown() {
        assertThrows(NotFoundException.class, () -> orderService.deleteOrder(new UUID(2, 2).toString()));
    }

    @Test
    void givenExistingOrder_whenUpdateOrder_thenOrderIsUpdated() {
        orderDTO.setQuantity(5);
        orderService.updateOrder(orderSaved.getUuid().toString(), orderDTO);
        OrderDTO orderUpdated = orderService.getOrder(orderSaved.getUuid().toString());
        assertEquals(orderDTO.getQuantity(), orderUpdated.getQuantity());
    }

    @Test
    void givenNotExistingOrder_whenUpdateOrder_thenNotFoundExceptionIsThrown() {
        assertThrows(NotFoundException.class, () -> orderService.updateOrder(new UUID(2, 2).toString(), orderDTO));
    }

    @Test
    void givenExistingOrders_whenGetOrders_thenOrdersAreReturned() {
        OrderDTO orderDTO2 = new OrderDTO(new UUID(2, 2), clientSaved.getDocument(), productSaved.getUuid().toString(),
                3,
                "Extra information");
        orderService.createOrder(orderDTO2);

        assertEquals(2, orderService.getOrders().size());
    }

    @Test
    void givenExistingOrders_whenGetSalesReport_thenSalesReportIsReturned() {
        String startDate = "20230101";
        String endDate = "20250101";

        SalesReportDTO salesReport = orderService.findProductSalesReportByDates(startDate, endDate);

        assertEquals(1, salesReport.getProductSales().size());
        assertEquals(1, salesReport.getMostSoldProducts().size());
        assertEquals(1, salesReport.getLeastSoldProducts().size());
    }
}
