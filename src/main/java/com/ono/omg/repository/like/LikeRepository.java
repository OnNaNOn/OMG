package com.ono.omg.repository.like;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Like;
import com.ono.omg.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByProductIdAndAccount(Long id, Account account);

    void deleteByProductId(Long productId);

    @Query("select l from Like l join fetch l.account a where a.id =:accountId")
    List<Like>findDetailsList(Pageable pageable, @Param("accountId")Long accountId);
}
