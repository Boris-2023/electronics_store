package ru.gb.electronicsstore.controller.web.admin;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.service.ProductService;

import java.util.Optional;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

    ProductService productService;

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/product-create")
    public String createProductForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "/admin/product-create";
    }

    @PostMapping("/product-create")
    public String createNewProduct(Product product) {

        if (productService.addProduct(product) != null) {
            return "redirect:/admin/products?created";
        } else {
            return "redirect:/admin/products?create_failed";
        }
    }

    @GetMapping("/product-delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        Boolean isSucceeded = productService.deleteProductById(id);
        if (isSucceeded) {
            return "redirect:/admin/products?deleted";
        }
        return "redirect:/admin/products?delete_failed";
    }

    @GetMapping("/product-update/{id}")
    public String updateUserForm(@PathVariable("id") long id, Model model) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            model.addAttribute("product", productOptional.get());
            return "admin/product-update";
        }
        return "redirect:/admin/products?search_failed";
    }

    @PostMapping("/product-update")
    public String updateUser(Product product) {
        productService.updateProductParameters(product.getId(), product);
        return "redirect:/admin/products?updated";
    }


}
