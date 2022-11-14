package com.ono.omg.repository;

import com.ono.omg.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductId(Long productId);
}
