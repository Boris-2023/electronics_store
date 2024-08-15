package ru.gb.electronicsstore.controller.web.admin;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.aspect.TrackUserAction;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.service.ProductService;

import java.util.Optional;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    ProductService productService;

    @TrackUserAction
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/admin/products";
    }

    @TrackUserAction
    @RequestMapping(value = "/product-create", method = RequestMethod.GET)
    public String createProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "/admin/product-create";
    }

    @TrackUserAction
    @RequestMapping(value = "/product-create", method = RequestMethod.POST)
    public String createNewProduct(Product product) {

        if (productService.addProduct(product)) {
            return "redirect:/admin/products?created";
        } else {
            return "redirect:/admin/products?create_failed";
        }
    }

    @TrackUserAction
    @RequestMapping(value = "/product-delete/{id}", method = RequestMethod.GET)
    public String deleteProduct(@PathVariable("id") long id) {
        Boolean isSucceeded = productService.deleteProductByIdWithOrderConstraint(id);
        if (isSucceeded) {
            return "redirect:/admin/products?deleted";
        }
        return "redirect:/admin/products?delete_failed";
    }

    @TrackUserAction
    @RequestMapping(value = "/product-update/{id}", method = RequestMethod.GET)
    public String updateProductForm(@PathVariable("id") long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "/admin/product-update";
        }
        return "redirect:/admin/products?search_failed";
    }

    @TrackUserAction
    @RequestMapping(value = "/product-update", method = RequestMethod.POST)
    public String updateProduct(Product product) {
        productService.updateProductParameters(product.getId(), product);
        return "redirect:/admin/products?updated";
    }

}
