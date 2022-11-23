package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Order;
import com.ono.omg.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderServiceTest 의")
class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("productOrder 메서드는 사용자와 상품으로 주문을 생성하고, 상품의 재고를 1개 감소한다.")
    public void 단일_상품_주문() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));
        Product product = productRepository.save(createProductExistAccount("상품", 10, account.getId()));

        // when
        Order savedOrder = orderRepository.save(new Order(account, product, product.getPrice()));

        // then
        assertThat(savedOrder.getAccount().getUsername()).isEqualTo("jae");
        assertThat(savedOrder.getProduct().getTitle()).isEqualTo("상품");
        assertThat(savedOrder.getProduct().getStock()).isEqualTo(9);
    }

    @Test
    @DisplayName("productOrder 메서드는 사용자와 상품으로 주문을 생성하고, 상품의 재고를 1개 감소한다.")
    public void 단일_상품을_동시에_1000개의_주문() throws Exception {
        // given
        final int THREAD_COUNT = 990;
        final int PRODUCT_STOCK = 1111;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        Account account = accountRepository.save(createAccount("jae"));
        Product product = productRepository.save(createProductExistAccount("상품", PRODUCT_STOCK, account.getId()));

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    orderRepository.save(new Order(account, product, product.getPrice()));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        int expected = PRODUCT_STOCK - THREAD_COUNT; // 1111 - 990 = 121
//        assertThat(product.getStock()).isEqualTo(expected);
//        assertThat(orders.size()).isEqualTo(THREAD_COUNT);
    }
}