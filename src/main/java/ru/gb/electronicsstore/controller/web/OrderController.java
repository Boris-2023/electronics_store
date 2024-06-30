package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.service.OrderService;
import ru.gb.electronicsstore.service.OrdersDetailsService;
import ru.gb.electronicsstore.service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/cart")
public class OrderController {

    OrderService orderService;
    OrdersDetailsService ordersDetailsService;
    UserService userService;

    @GetMapping("/order")
    public String displayNewOrder(Model model, @RequestParam(required = false) Long pay) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userName).orElse(null);

        if(pay==null) pay=0L;

        if (user != null) {
            Order order = orderService.getMostRecentOrderByUserId(user.getId());
            if (order != null) {

                // order
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd, HH:mm");
                model.addAttribute("order_number", order.getId());
                model.addAttribute("order_date", dateFormat.format(order.getOrderDate()));
                model.addAttribute("order_amount", order.getAmount());
                model.addAttribute("payment", pay);

                // products
                model.addAttribute("products", ordersDetailsService.getProductsDataForOrderById(order.getId()));

                // customer
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
