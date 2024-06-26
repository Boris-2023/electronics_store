package ru.gb.electronicsstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.electronicsstore.domain.OrdersDetails;

public interface OrdersDetailsRepository  extends JpaRepository<OrdersDetails, Long> {
}
