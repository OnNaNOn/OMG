package com.ono.omg.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("장바구니의")
class CartTest {

    @Nested
    @DisplayName("Create 메서드는 ")
    class Create {
        @Test
        @DisplayName("상품 번호와 주문자의 정보로 장바구니에 담는다.")
        public void 상품_장바구니_담기기() throws Exception {
            // given
            Account account = new Account(new AccountRegisterRequestDto("jae", "pw", "pw"));
            Product product = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, account.getId());
            Cart cart = new Cart(account, product.getId());

            // when & then
            assertThat(cart.getProductId()).isEqualTo(product.getId());
            assertThat(cart.getAccount().getUsername()).isEqualTo(account.getUsername());
            assertThat(cart.getIsDeleted()).isEqualTo("N");
        }


    }

    @Nested
    @DisplayName("Delete 메서드는")
    class Delete {

        @Test
        @DisplayName("장바구니에 담긴 걸 제거한다.")
        public void 장바구니_제거() throws Exception {
            // given
            Account account = new Account(new AccountRegisterRequestDto("jae", "pw", "pw"));
            Product product = new Product("computer", 1000000, "컴퓨터", "초고속 배송", 100, account.getId());
            Cart cart = new Cart(account, product.getId());

            // when
            cart.deleteCart();

            // then
            assertThat(cart.getIsDeleted()).isEqualTo("Y");
        }

    }


}