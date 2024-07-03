package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.User;
import ru.gb.electronicsstore.service.OrderService;
import ru.gb.electronicsstore.service.OrdersDetailsService;
import ru.gb.electronicsstore.service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class OrderController {

    OrderService orderService;
    OrdersDetailsService ordersDetailsService;
    UserService userService;

    @GetMapping("/cart/order")
    public String displayOrder(Model model) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userName).orElse(null);

        if (user != null) {
            Optional<Order> orderOptional = orderService.getMostRecentOrderByUserId(user.getId());
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();

                // order
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                model.addAttribute("order_number", order.getId());
                model.addAttribute("order_date", dateFormat.format(order.getOrderDate()));
                model.addAttribute("order_amount", order.getAmount());
                model.addAttribute("payment", order.getPaymentReference());

                // products
                model.addAttribute("products", ordersDetailsService.getProductsDataForOrderById(order.getId()));

                // customer
                model.addAttribute("customer", user.getFirstName() + " " + user.getLastName());
                model.addAttribute("phone", user.getPhone());
                model.addAttribute("address", user.getAddress());
                model.addAttribute("email", userName);
            } else {
                model.addAttribute("order_number", -1);
                //System.out.println("Order from WEB: No such ORDER!");
            }
        } else {
            //System.out.println("Order from WEB: No such USER!");
        }

        return "order";
    }

    @GetMapping("/profile/orders")
    public String getOrdersForCurrentUser(Model model) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userName).orElse(null);

        model.addAttribute("orders", orderService.getOrdersByUserId(user.getId()));

        return "user/orders";
    }

}
