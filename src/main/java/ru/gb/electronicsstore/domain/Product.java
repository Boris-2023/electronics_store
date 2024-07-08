package ru.gb.electronicsstore.domain;

import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return name.replace(" ", "").equalsIgnoreCase(product.name.replace(" ", ""))
                && manufacturer.replace(" ", "").equalsIgnoreCase(product.manufacturer.replace(" ", ""))
                && model.replace(" ", "").equalsIgnoreCase(product.model.replace(" ", ""))
                && countryOrigin.replace(" ", "").equalsIgnoreCase(product.countryOrigin.replace(" ", ""))
                && price.equals(product.price);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (manufacturer != null ? manufacturer.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (countryOrigin != null ? countryOrigin.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);

        return result;
    }
}
