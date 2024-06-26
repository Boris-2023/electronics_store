package ru.gb.electronicsstore.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.service.OrderService;
import ru.gb.electronicsstore.service.ProductService;
import ru.gb.electronicsstore.service.UserService;

import java.util.LinkedHashMap;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private ProductService productService;
    private OrderService orderService;
    private UserService userService;

    // list of products by ids provided, POST request used as GET does not allow BODY inside
    @PostMapping("/products")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestBody(required = false) List<Long> ids) {
        if (!ids.isEmpty()) {
            return new ResponseEntity<>(productService.getProductsByIds(ids), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PostMapping("/order")
    public ResponseEntity<List<Product>> makeNewOrder(@RequestBody LinkedHashMap<Long, Long> orderContent) {
        if (!orderContent.isEmpty()) {

            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            Long userId = userService.getUserByEmail(userName).orElse(null).getId();
            // System.out.println("\nHello from API controller: " + orderContent + " <- received as " + orderContent.getClass().getSimpleName() + "\n");

            boolean isCreated = orderService.makeNewOrder(orderContent, userId);

            if(isCreated) {
                return ResponseEntity.status(HttpStatus.OK).build();
            } else{
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
