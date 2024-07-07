package ru.gb.electronicsstore.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "date_order", nullable = false)
    private Timestamp orderDate;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String address;

    @Column(name = "status_order", length = 50, nullable = false)
    private String status;

    @Column(name = "date_update", nullable = false)
    private Timestamp lastUpdated;

    @Column(name = "payment_reference", nullable = false)
    private Long paymentReference = 0L;

}
