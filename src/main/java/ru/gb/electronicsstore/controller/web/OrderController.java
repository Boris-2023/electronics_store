package ru.gb.electronicsstore.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gb.electronicsstore.aspect.TrackUserAction;
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

    // display current order - most recent for this authorized user (taken from security context)
    @TrackUserAction
    @RequestMapping(value = "/cart/order", method = RequestMethod.GET)
    public String displayCurrentOrder(Model model) {

        // retrieves user
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOpt = userService.getUserByEmail(userName);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // retrieves current order by this user
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
            model.addAttribute("order_number", -1);
            //System.out.println("Order from WEB: No such USER!");
        }
        return "order";
    }

    // display all orders by this authorized user
    @TrackUserAction
    @RequestMapping(value = "/profile/orders", method = RequestMethod.GET)
    public String getOrdersForCurrentUser(Model model) {

        // gets user from security context
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(userName).orElse(null);

        model.addAttribute("orders", orderService.getOrderByUserId(user.getId()));

        return "user/orders";
    }

}
