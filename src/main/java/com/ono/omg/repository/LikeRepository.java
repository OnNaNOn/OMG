package com.ono.omg.repository;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByproduct(long productId);
    Optional<Like> findByUser_id(Account account);
}
