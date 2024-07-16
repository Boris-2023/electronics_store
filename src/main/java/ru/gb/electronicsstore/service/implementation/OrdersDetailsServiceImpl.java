package ru.gb.electronicsstore.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.OrdersDetails;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.repository.OrdersDetailsRepository;
import ru.gb.electronicsstore.service.OrdersDetailsService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class OrdersDetailsServiceImpl implements OrdersDetailsService {

    OrdersDetailsRepository oDetailsRepository;

    // gets all products listed for the order requested
    public List<OrdersDetails> getProductsByOrderId(Long id) {
        try {
            return oDetailsRepository.findAll().stream()
                    .filter(x -> (x.getOrder().getId()).equals(id))
                    .toList();
        } catch (Exception e) {
            System.out.println("Failed to connect database in getProductsByOrderId(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // gets products data for the order by its id provided
    public LinkedHashMap<String, List<String>> getProductsDataForOrderById(Long orderId) {
        try {
            LinkedHashMap<String, List<String>> products = new LinkedHashMap<>();

            List<OrdersDetails> details = getProductsByOrderId(orderId);

            String[] values = new String[3];
            Product product;

            for (OrdersDetails detail : details) {

                product = detail.getProduct();

                // price - from orderDetails! as it might be changed for the product already
                values[0] = String.valueOf(detail.getPrice());

                //qty and total price for this item
                values[1] = String.valueOf(detail.getQuantity());
                values[2] = String.valueOf((detail.getPrice()) * (detail.getQuantity()));
                products.put(product.getName() + " " + product.getManufacturer() + " " + product.getModel(), List.of(values));
            }
            return products;
        } catch (Exception e) {
            System.out.println("Failed to connect database in getProductsDataForOrderById(): " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }

}
