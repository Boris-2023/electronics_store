package ru.gb.electronicsstore.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.OrdersDetails;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.enums.OrderStatus;
import ru.gb.electronicsstore.repository.OrderRepository;
import ru.gb.electronicsstore.repository.OrdersDetailsRepository;
import ru.gb.electronicsstore.repository.ProductRepository;
import ru.gb.electronicsstore.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    OrdersDetailsRepository oDetailsRepository;

    public List<Order> getOrderByUserId(Long user_id) {
        return orderRepository.findAll().stream()
                .filter(x -> x.getUser().getId() == user_id)
                .toList();
    }

    public Order getOrderByUserAndStatus(Long user_id, OrderStatus status) {
        return orderRepository.findAll().stream()
                .filter(x -> Objects.equals(x.getUser().getId(), user_id) && x.getStatus().equals(status.name()))
                .findFirst()
                .orElseGet(null);
    }

    public boolean makeNewOrder(LinkedHashMap<Long, Long> content, String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Order order = new Order();
            List<OrdersDetails> detailsList = new ArrayList<>(content.size());

            double amount = 0;

            for (Long key : content.keySet()) {
                // finds product by its id
                Optional<Product> productOptional = productRepository.findById(key);
                OrdersDetails oDetails = new OrdersDetails();

                if (productOptional.isPresent()) {
                    Product product = productOptional.get();

                    oDetails.setOrder(order);
                    oDetails.setProduct(product);
                    oDetails.setQuantity(content.get(key));

                    detailsList.add(oDetails);

                    amount += oDetails.getQuantity() * product.getPrice();

                } else return false;

            }

            // fills in order
            order.setUser(user);
            order.setAmount(amount);
            order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
            order.setStatus(OrderStatus.CREATED.name());
            order.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));

            // order must be saved 1st as otherwise ordersDetails fail to save w/o order_id
            orderRepository.save(order);
            oDetailsRepository.saveAll(detailsList);

        } else return false;

        return true;
    }
}
