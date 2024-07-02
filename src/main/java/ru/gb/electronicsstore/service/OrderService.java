package ru.gb.electronicsstore.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.OrdersDetails;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.dto.PaymentDTO;
import ru.gb.electronicsstore.domain.enums.OrderStatus;
import ru.gb.electronicsstore.repository.OrderRepository;
import ru.gb.electronicsstore.repository.OrdersDetailsRepository;
import ru.gb.electronicsstore.repository.ProductRepository;
import ru.gb.electronicsstore.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    public Optional<Order> getMostRecentOrderByUserId(Long user_id) {
        return orderRepository.findAll().stream()
                .filter(x -> Objects.equals(x.getUser().getId(), user_id))
                .reduce((first, second) -> second);
    }

    @Transactional
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
                    oDetails.setPrice(product.getPrice());

                    // transfer products from stock to the order, adjust order qty if less in stock, false if product is out of stock
                    if (transferProductsFromStockToOrder(product, oDetails)) {
                        detailsList.add(oDetails);
                        amount += oDetails.getQuantity() * oDetails.getPrice();
                    }

                } else return false;

            }

            // fills in order data
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

    private boolean transferProductsFromStockToOrder(Product product, OrdersDetails currentDetails) {

        // adjusts order details if there is no enough product qty in stock
        if (currentDetails.getQuantity() > product.getQuantity()) currentDetails.setQuantity(product.getQuantity());

        // product out of stock
        if (currentDetails.getQuantity() == 0) return false;

        product.setQuantity(product.getQuantity() - currentDetails.getQuantity());
        productRepository.save(product);

        return true;
    }

    @Transactional
    public Long makePayment(PaymentDTO paymentDTO) {
        //System.out.println("\nService gets PaymentDTO: " + paymentDTO);

        Optional<User> userOptional = userRepository.findByEmail(paymentDTO.getUser());
        Optional<Order> orderOptional = orderRepository.findById(paymentDTO.getOrder());

        if (userOptional.isPresent() && orderOptional.isPresent()) {

            // here will be the payment routine
            long paymentReference = ThreadLocalRandom.current().nextLong(0, 10_000_000);
            Order order = orderOptional.get();

            // check the payment is succeeded - here 30% fail as a dummy
            if (paymentReference > 3_000_000) {

                order.setStatus(OrderStatus.PAID.name());
                order.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));

            } else { // fails to pay
                paymentReference = -1L;
            }
            order.setPaymentReference(paymentReference);
            orderRepository.save(order);

            return paymentReference;
        }
        return -1L;
    }
}
