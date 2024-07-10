package ru.gb.electronicsstore.service;

import ru.gb.electronicsstore.domain.Product;

import java.util.List;
import java.util.Optional;


public interface ProductService {

    List<Product> getAllProducts();

    List<Product> getActiveProductsInStockByText(String text);

    Optional<Product> getActiveProductById(Long id);

    Optional<Product> getProductById(Long id);

    List<Product> getProductsByIds(List<Long> ids);
    
    Boolean addProduct(Product product);

    boolean updateProductParameters(Long id, Product product);

    Boolean deleteProductByIdWithOrderConstraint(Long id);
}
