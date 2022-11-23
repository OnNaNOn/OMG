package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.exception.CustomCommonException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import static com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;
import static com.ono.omg.dto.response.ProductResponseDto.ProductResDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThat(allSavedProducts.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("createProduct 메서드는 상품을 등록한다.")
    public void 상품_등록_로그인O() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));
        ProductReqDto productReqDto = createProductInfoWithLogin("항해", 100);

        // when
        String createOK = productService.createProduct(productReqDto, account);

        // then
        assertThat(createOK).isEqualTo("상품 등록 완료");
    }

    @Test
    @DisplayName("createProduct 메서드는 상품을 등록할 때 로그인하지 않은 사용자라면 예외를 발생시킨다.")
    public void 상품_등록_로그인X() throws Exception {
        // given & when
        CustomCommonException exception = assertThrows(CustomCommonException.class, () -> {
            productService.createProduct(createProductInfoWithLogin("항해", 10), createAccount("kim"));
        });

        // then
        assertThat("존재하지 않는 사용자입니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("updateProduct 메서드는 변경할 상품 정보로 변경한다.")
    public void 상품_수정() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));
        Product product = productRepository.save(createProductExistAccount("상품", 10, account.getId()));

        // when
        productService.updateProduct(product.getId(), createProductInfoWithLogin("D-30", 30), account);

        // then
        assertThat(product.getTitle()).isEqualTo("D-30");
        assertThat(product.getStock()).isEqualTo(30);
    }

    @Test
    @DisplayName("deleteProduct 메서드는 상품을 제거한다. 단, 상품의 YN 필드를 N으로 변경한다.")
    public void 상품_삭제() throws Exception {
        // given
        Product product = productRepository.save(createProduct("상품", 10));

        // when
        product.isDeleted();

        // then
        assertThat(product.getIsDeleted()).isEqualTo("Y");
    }

    @Test
    @DisplayName("searchProduct 메서드는 특정 한 개의 상품에 대해 조회한다.")
    public void 상품_단건_조회() throws Exception {
        // given
        Product product = productRepository.save(createProduct("상품", 10));

        // when
        ProductResDto findProduct = productService.searchProduct(product.getId());

        // then
        assertThat(findProduct.getTitle()).isEqualTo("상품");
        assertThat(findProduct.getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("registerDetailsProduct 메서드는 마이페이지에서 로그인한 사용자의 등록 상품 내역을 조회한다.")
    public void 특정_사용자의_등록_상품_조회() throws Exception {
        // given
        final Integer MAX = 5;

        Pageable pageable = Pageable.ofSize(5);

        Account account = accountRepository.save(createAccount("jae"));
        for (int i = 1; i <= MAX; i++) {
            productRepository.save(createProductExistAccount("상품" + i, i, account.getId()));
        }

        // when
        List<MainPageOrdersResponseDto> registerProduct = productService.registerDetailsProduct(pageable, account);

        // then
        assertThat(registerProduct.size()).isEqualTo(MAX);
        assertThat(registerProduct.get(MAX - 1).getTitle()).isEqualTo("상품" + MAX);

    }

}