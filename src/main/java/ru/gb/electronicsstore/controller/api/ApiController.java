package ru.gb.electronicsstore.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.electronicsstore.domain.Product;
import ru.gb.electronicsstore.service.ProductService;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private ProductService service;

    // list of products by ids provided, POST request used as GET does not allow BODY inside
    @PostMapping("/products")
    public ResponseEntity<List<Product>> getProductsByIds(@RequestBody(required = false) List<Long> ids) {
        if (!ids.isEmpty()) {
            //System.out.println("\nHello from API controller: " + ids + " <- received as " + ids.getClass().getSimpleName() + "\n");
            return new ResponseEntity<>(service.getProductsByIds(ids), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
