package ru.gb.electronicsstore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String manufacturer;

    @Column(length = 100, nullable = false)
    private String model;

    @Column(name = "country_origin", length = 50)
    private String countryOrigin;

    @Column(name = "descript")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Long quantity;

}
