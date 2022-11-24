package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ReviewServiceTest 의")
class ReviewServiceTest extends ServiceTest {

    @Test
    @DisplayName("registerReview 메서드는 특정 상품의 리뷰를 등록한다.")
    public void 상품_리뷰_등록() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));
        Product product = productRepository.save(createProductExistAccount("상품", 10, account.getId()));

        // when
//        productService.re

        // then
    }


}