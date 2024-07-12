package ru.gb.electronicsstore.controller.web.admin;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.aspect.TrackUserAction;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.enums.OrderStatus;
import ru.gb.electronicsstore.service.OrderService;

import java.util.Optional;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminOrderController {

    OrderService orderService;

    @TrackUserAction
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @TrackUserAction
    @RequestMapping(value = "/order-delete/{id}", method = RequestMethod.GET)
    public String deleteOrderWithStatusConstraints(@PathVariable("id") long id) {
        boolean isSucceeded = orderService.deleteOrderByIdWithStatusConstraint(id,
                new OrderStatus[]{OrderStatus.CREATED, OrderStatus.COMPLETED});
        if (isSucceeded) {
            return "redirect:/admin/orders?deleted";
        }
        return "redirect:/admin/orders?delete_failed";
    }

    @TrackUserAction
    @RequestMapping(value = "/order-update/{id}", method = RequestMethod.GET)
    public String updateOrderForm(@PathVariable("id") long id, Model model) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            return "admin/order-update";
        }
        return "redirect:/admin/orders?search_failed";
    }

    @TrackUserAction
    @RequestMapping(value = "/order-update", method = RequestMethod.POST)
    public String updateOrder(Order order) {
        orderService.updateOrderParameters(order.getId(), order);
        return "redirect:/admin/orders?updated";
    }

}
