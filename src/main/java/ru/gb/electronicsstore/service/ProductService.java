package ru.gb.electronicsstore.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
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

   public List<Product> getProductsByText(String text) {
        final String testString = text.replace(" ", "").toLowerCase();
        return repository.findAll().stream()
                .filter(x -> (x.getName() + x.getManufacturer() + x.getModel()).toLowerCase().contains(testString))
                .toList();
    }

    public Product getProductById(Long id) {
        return repository.findById(id).orElseGet(null);
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        return repository.findAll().stream()
                .filter(x -> ids.contains(x.getId()))
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
