package ru.gb.electronicsstore.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.OrdersDetails;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.repository.OrdersDetailsRepository;

import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class OrdersDetailsService {

    OrdersDetailsRepository repository;

    public List<OrdersDetails> getProductsByOrderId(Long id) {
        return repository.findAll().stream()
                .filter(x -> (x.getOrder().getId()).equals(id))
                .toList();
    }

    public LinkedHashMap<String, List<String>> getProductsDataForOrderById(Long orderId) {
        LinkedHashMap<String, List<String>> products = new LinkedHashMap<>();

        List<OrdersDetails> details = getProductsByOrderId(orderId);

        String[] values = new String[3];
        Product product;

        for (int i = 0; i < details.size(); i++) {

            product = details.get(i).getProduct();

            // price of the product, its qty and total price for this item
            values[0] = String.valueOf(product.getPrice());

            values[1] = String.valueOf(details.get(i).getQuantity());
            values[2] = String.valueOf((product.getPrice()) * (details.get(i).getQuantity()));
            products.put(product.getName() + " " + product.getManufacturer() + " " + product.getModel(), List.of(values));


        }
        return products;
    }

}
