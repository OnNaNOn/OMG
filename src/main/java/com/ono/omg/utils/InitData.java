package com.ono.omg.utils;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.AccountType;
import com.ono.omg.domain.DeletedType;
import com.ono.omg.domain.Product;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitData {

    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 10; i++) {
            accountRepository.save(new Account(AccountType.ROLE_STANDARD, "안" + i, passwordEncoder.encode("감난다"), DeletedType.DELETE_NO));
            productRepository.save(new Product("피카츄" + i, 1000 + i, "포켓몬", "초고속 배송", 15 + i, (long) i));
        }
    }
}
