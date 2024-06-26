package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.service.ProductService;

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

        model.addAttribute("products", service.getProductsInStockByText(""));

        return "products";
    }


    // for search
    @GetMapping("/products")
    public String getProductsByText(Model model, @RequestParam(defaultValue = "") String search) {

        model.addAttribute("products", service.getProductsInStockByText(search));
        model.addAttribute("value", search);

        return "products";
    }

    // return a list of products based on id numbers provided
    @GetMapping("/cart")
    public String getProductsByIds() {

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
