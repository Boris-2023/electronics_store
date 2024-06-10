package ru.gb.electronicsstore.service;

import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private ProductRepository repository;

    public Product addProduct(Product product) {
        repository.save(product);
        return product;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> getProductsWithinPriceRange(Double low, Double high) {
        return repository.findAll().stream()
                .filter(x -> x.getPrice() >= low && x.getPrice()<= high)
                .toList();
    }

    public Product updateProductParameters(Long id, Product product) {
        Optional<Product> optionalProduct = repository.findById(id);

        if (optionalProduct.isPresent()) {
            Product destinationProduct = optionalProduct.get();
            destinationProduct.setCountryOrigin(product.getCountryOrigin());
            destinationProduct.setManufacturer(product.getManufacturer());
            destinationProduct.setQuantity(product.getQuantity());
            destinationProduct.setPrice(product.getPrice());
            return repository.save(destinationProduct);
        } else {
            throw new IllegalArgumentException("No Product found with id: " + id);
        }
    }

    public void deleteProductById(Long id) {
        Product product = repository.findById(id).orElseGet(null);
        if (product != null) repository.delete(product);
    }
}