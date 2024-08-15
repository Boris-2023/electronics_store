package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.aspect.TrackUserAction;
import ru.gb.electronicsstore.service.ProductService;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    // display all products with photos
    @TrackUserAction
    @RequestMapping(method = RequestMethod.GET)
    public String getAllProducts(Model model) {

        model.addAttribute("products", productService.getActiveProductsInStockByText(""));

        return "products";
    }

    // display all products with photos, which match search phrase (input on top of the page)
    @TrackUserAction
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getActiveProductsByText(Model model, @RequestParam(defaultValue = "") String search) {

        model.addAttribute("products", productService.getActiveProductsInStockByText(search));
        model.addAttribute("value", search);

        return "products";
    }

    // display the cart, most routine is performed by JS-code in the page
    @TrackUserAction
    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public String getProductsByIds() {

        return "cart";
    }

    // display particular product by its id
    @TrackUserAction
    @RequestMapping(value = "/products/card", method = RequestMethod.GET)
    public String getProductsById(Model model, @RequestParam Long id) {
        model.addAttribute("product", productService.getActiveProductById(id).orElseGet(null));

        return "card";
    }
}
