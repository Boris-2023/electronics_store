package ru.gb.electronicsstore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
public class Product implements Serializable {

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

    @Column(nullable = false)
    private Boolean isActive;

    public boolean equals(Product product){
        return this.getName().replace(" ", "").equalsIgnoreCase(product.getName().replace(" ", ""))
                && this.getManufacturer().replace(" ", "").equalsIgnoreCase(product.getManufacturer().replace(" ", ""))
                && this.getModel().replace(" ", "").equalsIgnoreCase(product.getModel().replace(" ", ""))
                && this.getCountryOrigin().replace(" ", "").equalsIgnoreCase(product.getCountryOrigin().replace(" ", ""))
                && this.getPrice().equals(product.getPrice());
    }
}
