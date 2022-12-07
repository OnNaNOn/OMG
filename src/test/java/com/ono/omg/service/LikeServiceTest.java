package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.exception.CustomCommonException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DisplayName("LikeServiceTest 의")
class LikeServiceTest extends ServiceTest {

    @Test
    @DisplayName("addLikes 메서드는 상품에 좋아요를 누른다.")
    public void 상품_좋아요() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));
        Product product = productRepository.save(createProductExistAccount("상품", 10, account.getId()));

        // when
        String result = likeService.addLikes(product.getId(), account.getId());

        // then
        assertThat(result).isEqualTo("좋아요 완료");
    }

    @Test
    @DisplayName("addLikes 메서드는 상품에 좋아요가 누른 상태라면 좋아요가 취소된다.")
    public void 상품_좋아요_취소() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));
        Product product = productRepository.save(createProductExistAccount("상품", 10, account.getId()));
        likeService.addLikes(product.getId(), account.getId());

        // when
        String result = likeService.addLikes(product.getId(), account.getId());

        // then
        assertThat(result).isEqualTo("좋아요가 취소되었습니다");
    }

    @Test
    @DisplayName("addLikes 메서드는 로그인하지 않은 사용자라면 예외가 발생한다.")
    public void 좋아요_유효하지_않은_유저_예외() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));
        Product product = productRepository.save(createProductExistAccount("상품", 10, account.getId()));


        // then
        CustomCommonException exception = assertThrows(CustomCommonException.class, () -> {
            likeService.addLikes(product.getId(), 50L);
        });

        // when
        assertThat("존재하지 않는 사용자입니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("addLikes 메서드는 상품이 존재하지 않으면 예외가 발생한다.")
    public void 좋아요_유효하지_않은_상품_예외() throws Exception {
        // given
        Account account = accountRepository.save(createAccount("jae"));

        // then
        CustomCommonException exception = assertThrows(CustomCommonException.class, () -> {
            likeService.addLikes(500L, account.getId());
        });

        // when
        assertThat("상품이 존재하지 않습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("likesDetails 메서드는 특정 사용자가 누른 좋아요를 반환한다.")
    public void 좋아요_목록_조회() throws Exception {
        // given

        // when

        // then
    }

}