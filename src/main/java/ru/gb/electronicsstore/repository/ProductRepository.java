package ru.gb.electronicsstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.electronicsstore.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
