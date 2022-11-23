package com.ono.omg.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;

@DisplayName("ProductServiceTest 의")
class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("findAllSavedProducts 메서드는 모든 상품을 조회한다.")
    public void 모든_상품_조회() throws Exception {
        // given
        int max = 5;
        for (int i = 1; i <= max; i++) {
            productRepository.save(createProduct("선장" + i, i));
        }

        // when
        List<MainPageResponseDto> allSavedProducts = productService.findAllSavedProducts();

        // then
        Assertions.assertThat(allSavedProducts.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("createProduct 메서드는 상품을 등록한다.")
    public void 상품_등록_로그인O() throws Exception {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("createProduct 메서드는 상품을 등록할 때 로그인하지 않은 사용자라면 예외를 발생시킨다.")
    public void 상품_등록_로그인X() throws Exception {
        // given
//        productService.createProduct()

        // when

        // then
    }



}