package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Order;
import com.ono.omg.domain.Product;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("OrderServiceTest 의")
class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("productOrder 메서드는 사용자와 상품으로 주문을 생성하고, 상품의 재고를 1개 감소한다.")
    public void 단일_상품_주문() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));
        Product product = productRepository.save(createProductExistAccount("상품", 10, account.getId()));

        // when
        product.decreaseStock(1);
        Order order = new Order(account, product, product.getPrice());

        Product findProduct = productRepository.findById(product.getId()).get();

        // then
        assertThat(order.getAccount().getUsername()).isEqualTo("jae");
        assertThat(findProduct.getTitle()).isEqualTo("상품");
        assertThat(findProduct.getStock()).isEqualTo(9);
    }
}