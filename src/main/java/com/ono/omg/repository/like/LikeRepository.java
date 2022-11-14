package com.ono.omg.repository.like;

import com.ono.omg.domain.Like;
import com.ono.omg.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByProductAndUserid(Product product, Long id);
}
