package ru.gb.electronicsstore.service.implementation;

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
import ru.gb.electronicsstore.service.OrderService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    OrdersDetailsRepository oDetailsRepository;

    // return a list of all orders in database
    public List<Order> getAllOrders() {
        try {
            return orderRepository.findAll();
        } catch (Exception e) {
            System.out.println("Failed to connect database in getAllOrders(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // gets order by its id
    public Optional<Order> getOrderById(Long id) {
        try {
            return orderRepository.findById(id);
        } catch (Exception e) {
            System.out.println("Failed to connect database in getOrderById(): " + e.getMessage());
            return Optional.empty();
        }
    }

    // gets order by its user's id
    public List<Order> getOrderByUserId(Long userId) {
        try {
            return orderRepository.findAll().stream()
                    .filter(x -> x.getUser().getId().equals(userId))
                    .toList();
        } catch (Exception e) {
            System.out.println("Failed to connect database in getOrderByUserId(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Order getOrderByUserAndStatus(Long user_id, OrderStatus status) {
        try {
            return orderRepository.findAll().stream()
                    .filter(x -> Objects.equals(x.getUser().getId(), user_id) && x.getStatus().equals(status.name()))
                    .findFirst()
                    .orElseGet(null);
        } catch (Exception e) {
            System.out.println("Failed to connect database in getOrderByUserAndStatus(): " + e.getMessage());
            return null;
        }
    }

    // gets last listed order for the user - to display current order
    public Optional<Order> getMostRecentOrderByUserId(Long user_id) {
        try {
            return orderRepository.findAll().stream()
                    .filter(x -> Objects.equals(x.getUser().getId(), user_id))
                    .reduce((first, second) -> second);
        } catch (Exception e) {
            System.out.println("Failed to connect database in getMostRecentOrderByUserId(): " + e.getMessage());
            return Optional.empty();
        }
    }

    // makes new order
    @Transactional
    public Optional<Order> makeNewOrder(LinkedHashMap<Long, Long> content, String userEmail) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(userEmail);
            Order order = new Order();

            if (userOptional.isPresent()) {
                User user = userOptional.get();
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
                        if (transferProductFromStockToOrder(product, oDetails)) {
                            detailsList.add(oDetails);
                            amount += oDetails.getQuantity() * oDetails.getPrice();
                        }

                    } else return Optional.empty();

                }

                // fills in order data
                order.setUser(user);
                order.setAmount(amount);
                order.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));

                // contact & address must be fixed on order creation as user may change'em in profile later
                order.setContact(user.getPhone());
                order.setAddress(user.getAddress());

                order.setStatus(OrderStatus.CREATED.name());
                order.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));

                // order must be saved 1st as otherwise ordersDetails fail to save w/o order_id
                orderRepository.save(order);
                oDetailsRepository.saveAll(detailsList);

            } else return Optional.empty();

            return Optional.of(order);

        } catch (Exception e) {
            System.out.println("Failed to connect database in makeNewOrder(): " + e.getMessage());
            return Optional.empty();
        }
    }

    // when order is made the products from store's stock are passed to the order
    public boolean transferProductFromStockToOrder(Product product, OrdersDetails currentDetails) {
        try {
            // adjusts order details if there is no enough product qty in stock
            if (currentDetails.getQuantity() > product.getQuantity()) currentDetails.setQuantity(product.getQuantity());

            // product out of stock
            if (currentDetails.getQuantity() == 0) return false;

            product.setQuantity(product.getQuantity() - currentDetails.getQuantity());
            productRepository.save(product);

            return true;

        } catch (Exception e) {
            System.out.println("Failed to connect database in transferProductFromStockToOrder(): " + e.getMessage());
            return false;
        }
    }

    // simulation of the payment (30% fails, 70% succeed)
    @Transactional
    public Long makePayment(PaymentDTO paymentDTO) {
        try {
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
        } catch (Exception e) {
            System.out.println("Failed to connect database in makePayment(): " + e.getMessage());
            return -2L;
        }
    }

    public boolean deleteOrderById(Long id) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(id);
            if (orderOptional.isPresent()) {
                orderRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to connect database in deleteOrderById(): " + e.getMessage());
            return false;
        }
    }

    // deletes order with the database, but with constraint: CREATED or COMPLETED only
    @Transactional
    public boolean deleteOrderByIdWithStatusConstraint(Long id, OrderStatus[] statusesAllowed) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(id);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                if (Arrays.stream(statusesAllowed)
                        .anyMatch(x -> x.name().equalsIgnoreCase(order.getStatus()))) {

                    // for yet unpaid orders - return all order's products back to stock
                    if (order.getStatus().equalsIgnoreCase(OrderStatus.CREATED.name())) {
                        List<OrdersDetails> ordersDetails = oDetailsRepository.findByOrder(order);
                        if (!ordersDetails.isEmpty()) {
                            transferProductsFromCancelledOrderToStock(ordersDetails);
                        }
                    }
                    orderRepository.delete(order);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Failed to connect database in deleteOrderByIdWithStatusConstraint(): " + e.getMessage());
            return false;
        }
    }

    // transfer products back to store's stock - for cancelled orders
    @Transactional
    public boolean transferProductsFromCancelledOrderToStock(List<OrdersDetails> ordersDetails) {
        try {
            for (OrdersDetails oDetails : ordersDetails) {
                Optional<Product> productOptional = productRepository.findById(oDetails.getProduct().getId());
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    product.setQuantity(product.getQuantity() + oDetails.getQuantity());
                    productRepository.save(product);
                } else {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Failed to connect database in transferProductsFromCancelledOrderToStock(): " + e.getMessage());
            return false;
        }
    }

    public boolean deleteOrdersByStatus(String status) {
        try {
            List<Order> orders = orderRepository.findAll().stream()
                    .filter(x -> Objects.equals(x.getStatus(), status))
                    .toList();

            if (!orders.isEmpty()) {
                orderRepository.deleteAll(orders);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to connect database in deleteOrderByStatus(): " + e.getMessage());
            return false;
        }
    }

    // updates Status, Contact and Address for the order
    public boolean updateOrderParameters(Long id, Order order) {
        try {
            Optional<Order> optionalOrder = orderRepository.findById(id);

            if (optionalOrder.isPresent()) {
                Order destinationOrder = optionalOrder.get();

                // not all the parameters can be updated
                destinationOrder.setStatus(order.getStatus());
                destinationOrder.setContact(order.getContact());
                destinationOrder.setAddress(order.getAddress());

                orderRepository.save(destinationOrder);

                return true;
            } else {
                return false;
                //throw new IllegalArgumentException("No Order found with id: " + id);
            }
        } catch (Exception e) {
            System.out.println("Failed to connect database in updateOrderParameters(): " + e.getMessage());
            return false;
        }
    }

}
