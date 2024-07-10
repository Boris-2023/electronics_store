package ru.gb.electronicsstore.service;

import ru.gb.electronicsstore.domain.OrdersDetails;

import java.util.LinkedHashMap;
import java.util.List;

public interface OrdersDetailsService {

    List<OrdersDetails> getProductsByOrderId(Long id);

    LinkedHashMap<String, List<String>> getProductsDataForOrderById(Long orderId);

}
