package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("ConcurrentOrderServiceTest 의")
public class ConcurrentOrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void beforeEach() {
        orderRepository.deleteAll();
        accountRepository.deleteAll();
        productRepository.deleteAll();
    }

    @AfterEach
    public void afterEach() {
        orderRepository.deleteAll();
        accountRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("productOrderWithPessimisticLock( 비관적 락 ) 메서드는 사용자와 상품으로 주문을 생성하고, 상품의 재고를 1개 감소한다.")
    public void 단일_상품을_동시에_1000개의_주문_비관적_락() throws Exception {
        // given
        final int PRODUCT_STOCK = 10;
        final int THREAD_COUNT = 10; // PRODUCT_STOCK = 1000, THREAD_COUNT = 20000, FixedThreadPool = 32 ---> 13sec 715ms
        final long PRODUCT_ID = 10000L;
        final int EXPECTED = PRODUCT_STOCK - THREAD_COUNT; // 1030 - 1000 = 30

//        Account account = accountRepository.save(new Account(new AccountRegisterRequestDto("jae", "pw", "pw")));

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        System.out.println("ConcurrentOrderServiceTest.단일_상품을_동시에_1000개의_주문_비관적_락");

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    orderService.productOrderWithPessimisticLock(PRODUCT_ID, 1L);
                    System.out.println("ConcurrentOrderServiceTest.단일_상품을_동시에_1000개의_주문_비관적_락");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Product savedProduct = productRepository.findById(PRODUCT_ID).get();
//        assertThat(orderRepository.findAll().size()).isEqualTo(THREAD_COUNT);
        assertThat(savedProduct.getStock()).isEqualTo(EXPECTED);

//        assertThat(orderRepository.findAll().size()).isEqualTo(PRODUCT_STOCK);
//        assertThat(savedProduct.getStock()).isEqualTo(0);
    }

////    @Test
//    @DisplayName("productOrderRedisson( Redisson ) 메서드는 사용자와 상품으로 주문을 생성하고, 상품의 재고를 1개 감소한다.")
//    public void 단일_상품을_동시에_100개의_주문() throws Exception {
//        // given
//        final int PRODUCT_STOCK = 130;
//        final int THREAD_COUNT = 100;
//        final int EXPECTED = PRODUCT_STOCK - THREAD_COUNT; // 1030 - 1000 = 30
//
//        ExecutorService executorService = Executors.newFixedThreadPool(30);
//        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
//
//        Account account = accountRepository.save(new Account(new AccountRegisterRequestDto("jae", "pw", "pw")));
//        Product product = productRepository.save(new Product("상품", 1000, "카테고리", "배송상태", PRODUCT_STOCK, account.getId()));
//
//        // when
//        for (int i = 0; i < THREAD_COUNT; i++) {
//            executorService.submit(() -> {
//                try {
//                    orderService.productOrderRedisson(product.getId(), account);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();
//
//        // then
//        Product findProduct = productRepository.findById(product.getId()).get();
//
//        assertThat(findProduct.getStock()).isEqualTo(EXPECTED);
//        assertThat(orderRepository.findAll().size()).isEqualTo(THREAD_COUNT);
//    }
}
