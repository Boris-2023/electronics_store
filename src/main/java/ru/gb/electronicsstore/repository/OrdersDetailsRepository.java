package ru.gb.electronicsstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.OrdersDetails;

import java.util.List;

public interface OrdersDetailsRepository extends JpaRepository<OrdersDetails, Long> {
    public List<OrdersDetails> findByOrder(Order order);
}
