package ru.gb.electronicsstore.service;

import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.OrdersDetails;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.domain.dto.PaymentDTO;
import ru.gb.electronicsstore.domain.enums.OrderStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();

    Optional<Order> getOrderById(Long id);

    List<Order> getOrderByUserId(Long userId);

    Optional<Order> getMostRecentOrderByUserId(Long user_id);

    Optional<Order> makeNewOrder(LinkedHashMap<Long, Long> content, String userEmail);

    Long makePayment(PaymentDTO paymentDTO);

    boolean deleteOrderByIdWithStatusConstraint(Long id, OrderStatus[] statusesAllowed);

    boolean updateOrderParameters(Long id, Order order);

    boolean transferProductFromStockToOrder(Product product, OrdersDetails currentDetails);

    boolean transferProductsFromCancelledOrderToStock(List<OrdersDetails> ordersDetails);

}
