package ru.gb.electronicsstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.User;

import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public Optional<Order> findFirstByUser(User user);
}
