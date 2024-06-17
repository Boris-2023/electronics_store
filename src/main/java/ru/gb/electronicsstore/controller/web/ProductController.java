package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.service.ProductService;

import java.util.List;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class ProductController {
    private ProductService service;
    //       private final Counter addNewTaskCounter = Metrics.counter("new_product_counter");

    @PostMapping("/admin/add")
    public Product addProduct(@RequestBody Product product) {

        // addNewTaskCounter.increment();

        return service.addProduct(product);
    }

    @GetMapping
    public String getAllProducts(Model model) {

        model.addAttribute("products", service.getAllProducts());

        return "products";
    }


    // for search
    @GetMapping("/products")
    public String getProductsByText(Model model, @RequestParam(defaultValue = "") String search) {

        model.addAttribute("products", service.getProductsByText(search));
        model.addAttribute("value", search);
        model.addAttribute("auth", true);

        return "products";
    }

    // return a list of products based on id numbers provided
    @GetMapping("/cart")
    public String getProductsByIds(Model model) {

        System.out.println("\nHello from WEB controller\n");

        model.addAttribute("products", service.getProductsByIds(List.of(1L, 2L)));
//        model.addAttribute("products", service.getProductsByIds(ids));
//        model.addAttribute("auth", true);

        return "cart";
    }

    // page for particular product
    @GetMapping("/products/card")
    public String getProductsById(Model model, @RequestParam Long id) {

        model.addAttribute("product", service.getProductById(id));

        return "card";
    }

    @PutMapping("/admin/update")
    public Product updateProductParameters(@RequestParam Long id, @RequestBody Product product) {
        return service.updateProductParameters(id, product);
    }

    @DeleteMapping("/admin/delete")
    public void deleteProduct(@RequestParam Long id) {
        service.deleteProductById(id);
    }
}
