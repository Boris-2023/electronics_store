package ru.gb.electronicsstore.controller;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.service.ProductService;

import java.util.Random;

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

    @GetMapping("/test")
    public String getRandom(Model model) {
        int number = (new Random()).nextInt(100);
        model.addAttribute("number", number);
        return "random";
    }

//    @GetMapping("/products")
//    public String getProductsWithinPriceRange(Model model, @RequestParam(defaultValue = "0D") String low, @RequestParam(defaultValue = "100000000D") String high){
//
//        model.addAttribute("products", service.getProductsWithinPriceRange(Double.valueOf(low), Double.valueOf(high)));
//
//        return "products";
//    }

    @GetMapping
    public String getAllProducts(Model model){

        model.addAttribute("products", service.getAllProducts());

        return "products";
    }

//    @GetMapping("/products")
//    public String getNewProductsWithinPriceRange(Model model, @RequestParam(defaultValue = "0D") String low, @RequestParam(defaultValue = "100000000D") String high){
//
//        model.addAttribute("products", service.getProductsWithinPriceRange(Double.valueOf(low), Double.valueOf(high)));
//
//        return "products";
//    }

    // for search
    @GetMapping("/products")
    public String getProductsByText(Model model, @RequestParam(defaultValue = "") String search){

        model.addAttribute("products", service.getProductsByText(search));
        model.addAttribute("value", search);

        return "products";
    }

    // page for particular product
    @GetMapping("/products/card")
    public String getProductsById(Model model, @RequestParam Long id){

        model.addAttribute("product", service.getProductById(id));

        return "card";
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
