package com.ono.omg.repository.review;

import com.ono.omg.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.product.id =:productId and r.isDeleted='N'")
    List<Review> findAllByProductId(@Param("productId") Long productId);

    @Query("select r from Review r where r.userId =:accountId")
    List<Review> findReviewList(Pageable pageable, @Param("accountId") Long accountId);
}
