package ru.gb.electronicsstore.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.electronicsstore.domain.OrdersDetails;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.repository.OrdersDetailsRepository;
import ru.gb.electronicsstore.repository.ProductRepository;
import ru.gb.electronicsstore.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private OrdersDetailsRepository oDetailsRepository;

    // add product to the database
    public Boolean addProduct(Product product) {
        try {
            Optional<Product> productOptional = productRepository.findAll().stream()
                    .filter(x -> x.equals(product))
                    .findFirst();
            // do not save the product which is already in stock
            if (productOptional.isEmpty()) {
                productRepository.save(product);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to connect database in addProduct(): " + e.getMessage());
            return false;
        }
    }

    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            System.out.println("Fail to connect database in getAllProducts(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Product> getProductsWithinPriceRange(Double low, Double high) {
        try {
            return productRepository.findAll().stream()
                    .filter(x -> x.getPrice() >= low && x.getPrice() <= high)
                    .toList();
        } catch (Exception e) {
            System.out.println("Failed to connect database in getProductsWithinPriceRange(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // get all products from stock, which are allowed to be sold
    public List<Product> getActiveProductsInStockByText(String text) {
        try {
            final String testString = text.replace(" ", "").toLowerCase();
            return productRepository.findAll().stream()
                    .filter(x -> (x.getName() + x.getManufacturer() + x.getModel()).toLowerCase().contains(testString) && x.getQuantity() > 0 && x.getIsActive())
                    .toList();
        } catch (Exception e) {
            System.out.println("Failed to connect database in getActiveProductsInStockByText(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // returns a product by its id, but if it is allowed to be sold
    public Optional<Product> getActiveProductById(Long id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            // only active product can be returned - for clients
            if (productOptional.isPresent() && productOptional.get().getIsActive()) {
                return productRepository.findById(id);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("Failed to connect database in getActiveProductsById(): " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Product> getProductById(Long id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            System.out.println("Failed to connect database in getProductsById(): " + e.getMessage());
            return Optional.empty();
        }
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        try {
            return productRepository.findAll().stream()
                    .filter(x -> ids.contains(x.getId()))
                    .toList();
        } catch (Exception e) {
            System.out.println("Failed to connect database in getProductsByIds(): " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean updateProductParameters(Long id, Product product) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);

            if (optionalProduct.isPresent()) {
                Product destinationProduct = optionalProduct.get();

                // not all the parameters can be updated, otherwise - another product!
                destinationProduct.setDescription(product.getDescription());
                destinationProduct.setQuantity(product.getQuantity());
                destinationProduct.setPrice(product.getPrice());
                destinationProduct.setIsActive(product.getIsActive());

                productRepository.save(destinationProduct);
                return true;
            } else {
                return false;
                //throw new IllegalArgumentException("No Product found with id: " + id);
            }
        } catch (Exception e) {
            System.out.println("Failed to connect database in updateProductParameters(): " + e.getMessage());
            return false;
        }
    }

    // delete product if it is not included in any order
    public Boolean deleteProductByIdWithOrderConstraint(Long id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();

                // finds orders with this product included
                Optional<OrdersDetails> foundOrderOptional = oDetailsRepository.findAll().stream()
                        .filter(x -> x.getProduct().equals(product))
                        .findFirst();

                // can delete only products which are not included in any existing order
                if (foundOrderOptional.isEmpty()) {
                    productRepository.delete(product);
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
        } catch (Exception e) {
            System.out.println("Failed to connect database in deleteProductByIdWithOrderConstraint(): " + e.getMessage());
            return null;
        }
    }
}
