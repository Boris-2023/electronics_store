package ru.gb.electronicsstore.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.enums.OrderStatus;
import ru.gb.electronicsstore.repository.OrderRepository;
import ru.gb.electronicsstore.repository.ProductRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {

    OrderRepository orderRepository;
    ProductRepository productRepository;

    public List<Order> getOrderByUserId(Long user_id){
        return orderRepository.findAll().stream()
                .filter(x -> x.getUser().getId()==user_id)
                .toList();
    }

    public Order getOrderByUserAndStatus(Long user_id, OrderStatus status){
        return orderRepository.findAll().stream()
                .filter(x -> Objects.equals(x.getUser().getId(), user_id) && x.getStatus().equals(status.name()))
                .findFirst()
                .orElseGet(null);
    }

    public boolean makeNewOrder(LinkedHashMap<Long, Long> content, Long userId){

        return true;
    }
}
