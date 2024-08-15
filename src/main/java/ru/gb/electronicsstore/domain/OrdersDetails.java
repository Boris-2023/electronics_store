package ru.gb.electronicsstore.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @apiNote Class to keep products with their quantity for all order with ref to the order
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "orders_details")
public class OrdersDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column
    private Double price;

    @Column(nullable = false)
    private Long quantity;
}
