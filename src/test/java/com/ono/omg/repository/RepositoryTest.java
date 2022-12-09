package com.ono.omg.repository;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Order;
import com.ono.omg.domain.Product;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.token.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
abstract class RepositoryTest {

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ProductRepository productRepository;


    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    protected Account saveAccount(String username) {
        Account account = new Account(new AccountRegisterRequestDto(username, "pw", "pw"));
        return accountRepository.save(account);
    }

    protected Product createProduct(String title, Integer stock) {
        return new Product(title, 1000, "카테고리", "배송상태", stock, (long) stock);
    }

    protected Order saveOrders() {
        for (int i = 0; i < 5; i++) {
            Product givenProduct = productRepository.save(new Product("항해" + i, 1000 + i, "스파르타" + i, "빠른 배송", 10 + i, (long) i));
            Account givenAccount = accountRepository.save(new Account(new AccountRegisterRequestDto("선원" + i, "pw", "pw")));
            orderRepository.save(new Order(givenAccount, givenProduct, givenProduct.getPrice()));
        }
        Account givenAccount = accountRepository.save(new Account(new AccountRegisterRequestDto("선원", "pw", "pw")));
        Product givenProduct = productRepository.save(new Product("항해", 1000, "스파르타", "빠른 배송", 10, (long) 6L));
        return orderRepository.save(new Order(givenAccount, givenProduct, givenProduct.getPrice()));
    }
}