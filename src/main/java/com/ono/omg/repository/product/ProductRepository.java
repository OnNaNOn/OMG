package com.ono.omg.repository.product;

import com.ono.omg.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByUserid(Long id);
}
