package ru.gb.electronicsstore.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false)
    private double amount;

    @Column(name = "date_order", nullable = false)
    private Timestamp orderDate;

    @Column(name = "status_order", length = 50, nullable = false)
    private String status;

    @Column(name = "date_update", nullable = false)
    private Timestamp lastUpdated;
}
