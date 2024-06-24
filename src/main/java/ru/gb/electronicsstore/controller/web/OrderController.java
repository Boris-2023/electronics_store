package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/cart")
public class OrderController {

    @GetMapping("/order")
    public String makeNewOrder() {
        return "order";
    }
}
