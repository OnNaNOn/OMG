package com.ono.omg.repository.product;

import com.ono.omg.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Optional<Product> findBySellerId(Long id);

    @Query("select p from Product p where p.isDeleted='N'")
    Optional<Product> findProduct(Long id);

    @Query("select p from Product p where p.id =:productId")
    Product detailProduct(@Param("productId") Long productId);

    @Query("select p from Product p where p.sellerId =:accountId")
    List<Product> findRegisterProductList(Pageable pageable, @Param("accountId") Long accountId);
}
