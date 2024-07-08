package ru.gb.electronicsstore.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.OrdersDetails;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.repository.OrdersDetailsRepository;
import ru.gb.electronicsstore.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository repository;
    private OrdersDetailsRepository detailsRepository;

    public Boolean addProduct(Product product) {
        Optional<Product> productOptional = repository.findAll().stream()
                .filter(x -> x.equals(product))
                .findFirst();
        // do not save the product which is already in stock
        if (productOptional.isEmpty()) {
            repository.save(product);
            return true;
        } else {
            return false;
        }
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public List<Product> getProductsWithinPriceRange(Double low, Double high) {
        return repository.findAll().stream()
                .filter(x -> x.getPrice() >= low && x.getPrice() <= high)
                .toList();
    }

    public List<Product> getActiveProductsInStockByText(String text) {
        final String testString = text.replace(" ", "").toLowerCase();
        return repository.findAll().stream()
                .filter(x -> (x.getName() + x.getManufacturer() + x.getModel()).toLowerCase().contains(testString) && x.getQuantity() > 0 && x.getIsActive())
                .toList();
    }

    public Optional<Product> getActiveProductById(Long id) {
        Optional<Product> productOptional = repository.findById(id);
        // only active product can be returned - for clients
        if (productOptional.isPresent() && productOptional.get().getIsActive()) {
            return repository.findById(id);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        return repository.findAll().stream()
                .filter(x -> ids.contains(x.getId()))
                .toList();
    }

    public boolean updateProductParameters(Long id, Product product) {
        Optional<Product> optionalProduct = repository.findById(id);

        if (optionalProduct.isPresent()) {
            Product destinationProduct = optionalProduct.get();

            // not all the parameters can be updated, otherwise - another product!
            destinationProduct.setDescription(product.getDescription());
            destinationProduct.setQuantity(product.getQuantity());
            destinationProduct.setPrice(product.getPrice());
            destinationProduct.setIsActive(product.getIsActive());

            repository.save(destinationProduct);

            return true;
        } else {
            return false;
            //throw new IllegalArgumentException("No Product found with id: " + id);
        }
    }

    public Boolean deleteProductByIdWithOrderConstraint(Long id) {
        Optional<Product> productOptional = repository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // finds orders with this product included
            Optional<OrdersDetails> foundOrderOptional = detailsRepository.findAll().stream()
                    .filter(x -> x.getProduct().equals(product))
                    .findFirst();

            // can delete only products which are not included in any existing order
            if (foundOrderOptional.isEmpty()) {
                repository.delete(product);
                return true;
                // turns off its availability for clients
            } else {
                product.setIsActive(false);
                updateProductParameters(id, product);
                return false;
            }
        } else {
            return null;
        }
    }
}
