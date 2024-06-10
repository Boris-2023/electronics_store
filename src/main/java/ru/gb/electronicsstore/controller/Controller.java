package ru.gb.electronicsstore.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.service.ProductService;

import java.util.List;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class Controller {
    private ProductService service;
    //       private final Counter addNewTaskCounter = Metrics.counter("new_product_counter");

    @PostMapping("/admin/add")
    public Product addProduct(@RequestBody Product product){

        // addNewTaskCounter.increment();

        return service.addProduct(product);
    }

//    @GetMapping
//    public List<Product> getAllProducts(){
//        return service.getAllProducts();
//    }

    @GetMapping("/products")
    public List<Product> getProductsWithinPriceRange(@RequestParam(defaultValue = "0") String low, @RequestParam(defaultValue = "1 000 000 000") String high){
        return service.getProductsWithinPriceRange(Double.valueOf(low), Double.valueOf(high));
    }

    @PutMapping("/admin/update")
    public Product updateProductParameters(@RequestParam Long id, @RequestBody Product product){
        return service.updateProductParameters(id, product);
    }

    @DeleteMapping("/admin/delete")
    public void deleteProduct(@RequestParam Long id){
        service.deleteProductById(id);
    }
}
