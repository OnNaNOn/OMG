package com.ono.omg.repository.account;

import com.ono.omg.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);

    @Query("select a.username from Account a where a.id =:accountId")
    String findUsernameByAccountId(@Param("accountId")Long accountId);


}
