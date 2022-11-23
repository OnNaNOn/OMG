package com.ono.omg.domain;

import com.ono.omg.exception.CustomCommonException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("주문 의")
class OrderTest {

    @Nested
    @DisplayName("Create 메서드는")
    class Create {

        @Test
        @DisplayName("주문을 생성한다.")
        public void 주문_생성() throws Exception {
            // given
            Product givenProduct = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);
            Account givenAccount = new Account(new AccountRegisterRequestDto("jae", "pw", "pw"));

            // when
            Order createOrder = new Order(givenAccount, givenProduct, givenProduct.getPrice());

            // then
            assertThat(createOrder.getAccount()).isEqualTo(givenAccount);
            assertThat(createOrder.getProduct()).isEqualTo(givenProduct);
            assertThat(createOrder.getOrderType()).isEqualTo(OrderType.ORDER_OK);
        }
    }

    @Nested
    @DisplayName("Update 메서드는")
    class Update {

        @Test
        @DisplayName("재고가 있는 경우 현재 재고에 -1을 진행한다.")
        public void 재고_감소_예외X() throws Exception {
            // given
            Product zeroStockProduct = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);
            Account givenAccount = new Account(new AccountRegisterRequestDto("jae", "pw", "pw"));

            // when
            Order createOrder = new Order(givenAccount, zeroStockProduct, zeroStockProduct.getPrice());

            // then
            assertThat(createOrder.getProduct().getStock()).isEqualTo(99);
        }

        @Test
        @DisplayName("재고가 없을 시 예외가 발생한다.")
        public void 재고_감소_예외O() {
            // given
            Product zeroStockProduct = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 0, 2L);
            Account givenAccount = new Account(new AccountRegisterRequestDto("jae", "pw", "pw"));

            // 1. when & then
//        assertThatThrownBy(() -> new Order(givenAccount, zeroStockProduct, zeroStockProduct.getPrice()))
//                .isInstanceOf(CustomCommonException.class)
//                .hasMessageContainingAll("재고가 없습니다. 판매자에게 문의하세요");

            // 2. when And then
            CustomCommonException exception = assertThrows(CustomCommonException.class, () -> {
                new Order(givenAccount, zeroStockProduct, zeroStockProduct.getPrice());
            });
            assertEquals("재고가 없습니다. 판매자에게 문의하세요", exception.getMessage());
        }


        @Test
        @DisplayName("주문을 취소한다. 단, 재고는 유지한다.")
        public void 주문_취소() throws Exception {
            // given
            Product zeroStockProduct = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, 2L);
            Account givenAccount = new Account(new AccountRegisterRequestDto("jae", "pw", "pw"));
            Order createOrder = new Order(givenAccount, zeroStockProduct, zeroStockProduct.getPrice());

            // when
            createOrder.orderCancel();

            // then
            assertThat(createOrder.getOrderType()).isEqualTo(OrderType.ORDER_CANCEL);
        }
    }



}