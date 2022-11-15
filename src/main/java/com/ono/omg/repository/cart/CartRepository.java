package com.ono.omg.repository.cart;

import com.ono.omg.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("select c from Cart c join c.account a where c.productId =:productId and a.id =:accountId")
    Optional<Cart> findByProductIdAndAccountId(@Param("productId") Long productId, @Param("accountId") Long accountId);
}
