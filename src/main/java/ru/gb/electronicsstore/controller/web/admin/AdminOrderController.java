package ru.gb.electronicsstore.controller.web.admin;

import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.electronicsstore.domain.Order;
import ru.gb.electronicsstore.domain.enums.OrderStatus;
import ru.gb.electronicsstore.service.OrderService;

import java.util.Optional;

@org.springframework.stereotype.Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminOrderController {

    OrderService orderService;

    @GetMapping("/orders")
    public String getAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @GetMapping("/order-delete/{id}")
    public String deleteOrderWithStatusConstraints(@PathVariable("id") long id) {
        boolean isSucceeded = orderService.deleteOrderByIdWithStatusConstraint(id,
                new OrderStatus[]{OrderStatus.CREATED, OrderStatus.COMPLETED});
        if (isSucceeded) {
            return "redirect:/admin/orders?deleted";
        }
        return "redirect:/admin/orders?delete_failed";
    }

    @GetMapping("/order-update/{id}")
    public String updateOrderForm(@PathVariable("id") long id, Model model) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        if (orderOptional.isPresent()) {
            model.addAttribute("order", orderOptional.get());
            return "admin/order-update";
        }
        return "redirect:/admin/orders?search_failed";
    }

    @PostMapping("/order-update")
    public String updateOrder(Order order) {
        orderService.updateOrderParameters(order.getId(), order);
        return "redirect:/admin/orders?updated";
    }

}
