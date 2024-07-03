package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.service.ProductService;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class ProductController {

    private ProductService service;

    @GetMapping
    public String getAllProducts(Model model) {

        model.addAttribute("products", service.getActiveProductsInStockByText(""));

        return "products";
    }

    // for search
    @GetMapping("/products")
    public String getActiveProductsByText(Model model, @RequestParam(defaultValue = "") String search) {

        model.addAttribute("products", service.getActiveProductsInStockByText(search));
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
        model.addAttribute("product", service.getActiveProductById(id).orElseGet(null));

        return "card";
    }
}
