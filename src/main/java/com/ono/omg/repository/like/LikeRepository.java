package com.ono.omg.repository.like;

import com.ono.omg.domain.Like;
import com.ono.omg.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByProductAndUserid(Product product, Long id);

    void deleteByUserid(Long userid);
}
