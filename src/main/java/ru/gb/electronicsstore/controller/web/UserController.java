package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.service.UserService;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class UserController {

    private UserService service;

    @PostMapping("/auth/registration")
    public boolean addProduct(@RequestBody User user) {

        // addNewTaskCounter.increment();

        return service.addUser(user);
    }

    @GetMapping("/login")
    public String login() {
        return "login";

    }

}
