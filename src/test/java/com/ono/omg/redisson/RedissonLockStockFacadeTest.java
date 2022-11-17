package com.ono.omg.redisson;

import com.ono.omg.domain.*;
import com.ono.omg.dto.request.AccountRequestDto;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedissonLockStockFacadeTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AccountRepository accountRepository;

    Account account1;

    @BeforeEach
    public void insert() {
        Product product = new Product(25L,"이승우",20000,100,"카테고리","배달",25L,"N","imgUrl");

        productRepository.saveAndFlush(product);

        account1 = new Account(1L, AccountType.ROLE_ADMIN, "이승우", "1234", DeletedType.DELETE_NO);

        accountRepository.saveAndFlush(account1);

    }

    @AfterEach
    public void delete() {
        productRepository.deleteAll();
        accountRepository.deleteAll();
    }


    @Test
    public void 동시에_100개의요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.decrease(25L, account1);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product product = productRepository.findById(25L).orElseThrow();

        // 1000 - (1000 * 1) = 0
        assertEquals(0, product.getStock());
    }
}