package ru.gb.electronicsstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.electronicsstore.domain.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
