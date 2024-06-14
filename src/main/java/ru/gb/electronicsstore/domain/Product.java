package ru.gb.electronicsstore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String model;

    @Column(name = "country_origin")
    private String countryOrigin;

    @Column(name = "descript")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Long quantity;

}
