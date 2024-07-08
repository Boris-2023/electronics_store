package ru.gb.electronicsstore.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.electronicsstore.domain.OrdersDetails;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.repository.OrdersDetailsRepository;
import ru.gb.electronicsstore.repository.ProductRepository;
import ru.gb.electronicsstore.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProductServiceIntegrationTests {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrdersDetailsRepository detailsRepository;

    @Autowired
    private ProductService service;


    // test adding new product with check if such product exists already (=> cannot save)
    @Test
    public void addProductTest() {

        // precondition
        Product product = createProduct();

        Product productNew = new Product();
        productNew.setName("newName");
        productNew.setModel("newModel");

        given(productRepository.findAll()).willReturn(List.of(product)); // this product exists already

        // action
        service.addProduct(product); // must fail
        service.addProduct(productNew); // must succeed

        // result check
        verify(productRepository, never()).save(product);
        verify(productRepository).save(productNew);

    }

    // test product data update
    @Test
    public void updateProductParametersTest() {

        // precondition
        Product destinationProduct = createProduct();

        Product updatingProduct = createProduct();
        updatingProduct.setDescription("newDescription");
        updatingProduct.setQuantity(12345L);
        updatingProduct.setPrice(1000D);
        updatingProduct.setIsActive(false);

        given(productRepository.findById(1L)).willReturn(Optional.of(destinationProduct));// describe repo's behaviour

        // action
        service.updateProductParameters(1L, updatingProduct);

        // performance check
        verify(productRepository).save(updatingProduct);
    }

    // test deleting a product: can delete only product, which is not included into an of existing orders
    @Test
    public void deleteProductByIdWithOrderConstraintTest() {

        // precondition
        Product productNoOrdersIncludedIn = createProduct();

        Product productOrderIncludedIn = createProduct();
        productOrderIncludedIn.setName("newName"); //to differ from 1st product

        OrdersDetails oDetails = new OrdersDetails();
        oDetails.setId(1L);
        oDetails.setProduct(productOrderIncludedIn);

        // stubbing - describe repo's behaviour
        given(productRepository.findById(1L)).willReturn(Optional.of(productNoOrdersIncludedIn));
        given(productRepository.findById(2L)).willReturn(Optional.of(productOrderIncludedIn));
        given(detailsRepository.findAll()).willReturn(List.of(oDetails));

        // action
        service.deleteProductByIdWithOrderConstraint(1L);
        service.deleteProductByIdWithOrderConstraint(2L);

        // performance check
        verify(productRepository).delete(productNoOrdersIncludedIn);
        verify(productRepository, never()).delete(productOrderIncludedIn);

    }

    private static Product createProduct() {

        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setModel("model");
        product.setManufacturer("manufacturer");
        product.setCountryOrigin("country");
        product.setDescription("description");
        product.setPrice(1000D);
        product.setQuantity(100L);
        product.setIsActive(true);

        return product;
    }
}

