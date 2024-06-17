package ru.gb.electronicsstore.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private Date orderDate;
//    private double amount;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false, //
//    foreignKey = @ForeignKey(name = "ORDER_DETAIL_ORD_FK") )
}
