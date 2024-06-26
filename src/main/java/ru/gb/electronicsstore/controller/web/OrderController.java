package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.domain.enums.OrderStatus;
import ru.gb.electronicsstore.service.OrderService;
import ru.gb.electronicsstore.service.OrdersDetailsService;
import ru.gb.electronicsstore.service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/cart")
public class OrderController {

    OrderService orderService;
    OrdersDetailsService ordersDetailsService;
    UserService userService;

    @GetMapping("/order")
    public String displayNewOrder(Model model) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //System.out.println("\nName from WEB: " + userName + "\n");
        User user = userService.getUserByEmail(userName).orElse(null);

        if (user != null) {
            //System.out.println("\nUSER from WEB: " + user + "\n");
            Order order = orderService.getOrderByUserAndStatus(user.getId(), OrderStatus.CREATED);
            if (order != null) {
                //System.out.println("\nORDER from WEB: " + order + "\n");
                // order
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
                model.addAttribute("order_number", order.getId());
                model.addAttribute("order_date", dateFormat.format(order.getOrderDate()));
                model.addAttribute("order_amount", order.getAmount());

                // products
                HashMap<String, List<String>> products = ordersDetailsService.getProductsDataForOrderById(order.getId());
                //System.out.println("\nPRODUCTS from WEB: " + products + "\n");
                model.addAttribute("products", products);
                //model.addAttribute("products", ordersDetailsService.getProductsDataForOrderById(order.getId()));

                // user
                model.addAttribute("customer", user.getFirstName() + " " + user.getLastName());
                model.addAttribute("phone", user.getPhone());
                model.addAttribute("address", user.getAddress());
                model.addAttribute("email", userName);
            } else {
                System.out.println("Order from WEB: No such ORDER!");
            }
        } else {
            System.out.println("Order from WEB: No such USER!");
        }

        return "order";
    }
}
