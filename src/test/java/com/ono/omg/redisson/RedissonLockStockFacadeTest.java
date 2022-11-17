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

    @BeforeEach
    public void insert() {
//        productRepository.saveAndFlush(new Product("피카츄", 1000, "포켓몬", "초고속 배송", 1000, 1L));
        accountRepository.saveAndFlush(new Account(1L, AccountType.ROLE_ADMIN, "이승우", "1234", DeletedType.DELETE_NO));
        productRepository.saveAndFlush(new Product(202L, "라이츄", 1000, 1000, "포켓몬", "초고속 배송", 1L, "N", "king"));

// =================================================== #
//        accountRepository.saveAndFlush(new Account(1L, AccountType.ROLE_ADMIN, "이승우", "1234", DeletedType.DELETE_NO));
//
//        // 검증에 상관없는 요소 (ID값을 25로 준들, IDENTITY즉, Autoincreament로 했기떄문에 어차피 1로 저장됨.. 해당 요소는 소용이 없다)
//        Product product = new Product(25L,"이승우",20000,100,"카테고리","배달",12L,"N","imgUrl");
//
//        productRepository.saveAndFlush(product);
    }

    // 검증에 상관없는 요소 (삭제 순서가 바뀌어도 관련이 없다)
    @AfterEach
    public void delete() {
        productRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 990;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        System.out.println("========== 1 =========");

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.decrease(101L, accountRepository.findByUsername("이승우").get());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product product = productRepository.findById(1L).orElseThrow();

        System.out.println("product.getStock() = " + product.getStock());
        // 1000 - (1000 * 1) = 0
//        assertEquals(10, product.getStock());
    }
}