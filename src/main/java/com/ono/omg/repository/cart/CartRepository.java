package com.ono.omg.repository.cart;

import com.ono.omg.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByProductIdAndAccountId(Long productId, Long accountId);
}
