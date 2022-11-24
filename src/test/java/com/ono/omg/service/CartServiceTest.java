package com.ono.omg.service;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.CartResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CartServiceTest 의")
class CartServiceTest extends ServiceTest {

    @Test
    @DisplayName("inputProduct 메서드는 상품을 장바구니에 담는다.")
    public void 상품_장바구니_담기() throws Exception {
        // given
        UserDetailsImpl userDetails = new UserDetailsImpl(accountRepository.save(createAccount("jae")));
        Product product = productRepository.save(createProductExistAccount("상품", 10, userDetails.getAccount().getId()));

        // when
        Long accountId = cartService.inputProduct(product.getId(), userDetails);

        // then
        assertThat(accountId).isEqualTo(userDetails.getAccount().getId());
    }

    @Test
    @DisplayName("getCartList 메서드는 특정 사용자가 장바구니에 담은 상품을 조회한다.")
    public void 상품_장바구니_조회() throws Exception {
        // given
        UserDetailsImpl userDetails = new UserDetailsImpl(accountRepository.save(createAccount("jae")));

        Product product1 = productRepository.save(createProductExistAccount("상품1", 10, userDetails.getAccount().getId()));
        Product product2 = productRepository.save(createProductExistAccount("상품2", 10, userDetails.getAccount().getId()));

        // when
        cartService.inputProduct(product1.getId(), userDetails);
        cartService.inputProduct(product2.getId(), userDetails);

        // then
        List<CartResponseDto> carts = cartService.getCartList(userDetails);
        assertThat(carts.size()).isEqualTo(2);
    }
}