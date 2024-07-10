package ru.gb.electronicsstore.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gb.electronicsstore.domain.Order;

import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.enums.OrderStatus;
import ru.gb.electronicsstore.repository.OrderRepository;
import ru.gb.electronicsstore.repository.OrdersDetailsRepository;
import ru.gb.electronicsstore.repository.ProductRepository;
import ru.gb.electronicsstore.repository.UserRepository;
import ru.gb.electronicsstore.service.OrderService;
import ru.gb.electronicsstore.service.implementation.OrderServiceImpl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceModuleTests {

    @Captor
    private ArgumentCaptor<Order> userArgumentCaptor;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrdersDetailsRepository ordersDetailsRepository;

    @InjectMocks
    private OrderServiceImpl service;


    // order creation test
    @Test
    public void makeNewOrderTest() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        // precondition - user makes an order with two products inside counted 2 and 5 pcs at a price of 3000 and 2000
        LinkedHashMap<Long, Long> productsIdAndQty = new LinkedHashMap<>();
        productsIdAndQty.put(1L, 2L);
        productsIdAndQty.put(2L, 5L);

        User user = new User();
        user.setId(1L);
        user.setEmail("email");
        user.setPhone("123");
        user.setAddress("abc");

        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(3000.0);
        product1.setQuantity(500L);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(2000.0);
        product2.setQuantity(500L);

        given(userRepository.findByEmail("email")).willReturn(Optional.of(user));
        given(productRepository.findById(1L)).willReturn(Optional.of(product1));
        given(productRepository.findById(2L)).willReturn(Optional.of(product2));

        // action
        service.makeNewOrder(productsIdAndQty, user.getEmail());

        // result check
        verify(orderRepository).save(userArgumentCaptor.capture());
        assert (userArgumentCaptor.getValue().getUser().equals(user));
        assert (userArgumentCaptor.getValue().getAmount().equals(16000.0));
        assert (dateFormat.format(userArgumentCaptor.getValue().getOrderDate())
                .equals(dateFormat.format(Timestamp.valueOf(LocalDateTime.now()))));
        assert (dateFormat.format(userArgumentCaptor.getValue().getOrderDate())
                .equals(dateFormat.format(userArgumentCaptor.getValue().getLastUpdated())));
        assert (userArgumentCaptor.getValue().getContact().equals(user.getPhone()));
        assert (userArgumentCaptor.getValue().getAddress().equals(user.getAddress()));
        assert (userArgumentCaptor.getValue().getStatus().equals("CREATED"));
    }

    // order data update test
    @Test
    public void updateOrderParametersTest() {

        // preconditions
        Order orderUpdated = new Order();
        orderUpdated.setAddress("address");
        orderUpdated.setContact("phone");
        orderUpdated.setStatus("status");

        given(orderRepository.findById(1L)).willReturn(Optional.of(new Order()));// stubbing - repo's behaviour

        // action
        service.updateOrderParameters(1L, orderUpdated);

        // performance check
        verify(orderRepository).save(orderUpdated);
    }

    // test deletion of order: can delete order with status CREATED or COMPLETED only
    @Test
    public void deleteOrderByIdWithOrderConstraintTest() {

        // preconditions
        OrderStatus[] statusesAllowedToDelete = new OrderStatus[]{OrderStatus.CREATED, OrderStatus.COMPLETED};

        Order orderCreated = new Order();
        orderCreated.setStatus("CREATED");
        Order orderPaid = new Order();
        orderPaid.setStatus("PAID");
        Order orderCompleted = new Order();
        orderCompleted.setStatus("COMPLETED");

        // stubbing - describe repo's behaviour
        given(orderRepository.findById(1L)).willReturn(Optional.of(orderCreated));
        given(orderRepository.findById(2L)).willReturn(Optional.of(orderPaid));
        given(orderRepository.findById(3L)).willReturn(Optional.of(orderCompleted));

        // action
        service.deleteOrderByIdWithStatusConstraint(1L, statusesAllowedToDelete);
        service.deleteOrderByIdWithStatusConstraint(2L, statusesAllowedToDelete);
        service.deleteOrderByIdWithStatusConstraint(3L, statusesAllowedToDelete);

        // performance check
        verify(orderRepository).delete(orderCreated);
        verify(orderRepository, never()).delete(orderPaid);
        verify(orderRepository).delete(orderCompleted);

    }
}
