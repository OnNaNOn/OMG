package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.domain.Review;
import com.ono.omg.dto.request.ReviewRequestDto;
import com.ono.omg.dto.response.ReviewResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ono.omg.dto.request.ReviewRequestDto.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("ReviewServiceTest 의")
class ReviewServiceTest extends ServiceTest {

    @Test
    @DisplayName("registerReview 메서드는 특정 상품의 리뷰를 등록한다.")
    public void 상품_리뷰_등록() throws Exception {
        // given
        Account givenAccount = accountRepository.save(createAccount("jae"));
        Product givenProduct = productRepository.save(createProductExistAccount("상품", 10, givenAccount.getId()));
        ReviewRequestDto givenRequestDto = builder().reviewContent("내용").build();

        // when
        ReviewResponseDto review = reviewService.registerReview(givenProduct.getId(), givenAccount, givenRequestDto);

        // then
        assertThat(review.getReviewContent().equals("내용")).isTrue();
    }

    @Test
    @DisplayName("updateReview 메서드는 특정 상품의 리뷰를 수정한다.")
    public void 상품_리뷰_수정() throws Exception {
        // given
//        Account givenAccount = accountRepository.save(createAccount("jae"));
//        Product givenProduct = productRepository.save(createProductExistAccount("상품", 10, givenAccount.getId()));
//
//        ReviewResponseDto givenReviewDto = reviewService.registerReview(givenProduct.getId(), givenAccount, createReviewRequestDto("이전 내용"));
//        reviewService.updateReview(givenReviewDto.getReviewId(), givenAccount, createReviewRequestDto("새로운 내용"));
//
//        Review oldReview = reviewRepository.findById(givenReviewDto.getReviewId()).get();

        // when


        // then
//        Assertions.assertThat(oldReview.getReviewContent().equals("새로운 내용")).isTrue();
    }

    @Test
    @DisplayName("getReviewList 메서드는 특정 상품의 리뷰를 조회한다.")
    public void 상품_리뷰_전체_조회() throws Exception {
        Account givenAccount = accountRepository.save(createAccount("jae"));

        Product givenProduct1 = productRepository.save(createProductExistAccount("상품1", 10, givenAccount.getId()));
        Product givenProduct2 = productRepository.save(createProductExistAccount("상품2", 10, givenAccount.getId()));

        ReviewRequestDto givenRequestDto1 = createReviewRequestDto("상품 1번 리뷰");
        ReviewRequestDto givenRequestDto2 = createReviewRequestDto("상품 2번 리뷰");

        reviewService.registerReview(givenProduct1.getId(), givenAccount, givenRequestDto1);
        reviewService.registerReview(givenProduct2.getId(), givenAccount, givenRequestDto2);

        // when
        List<ReviewResponseDto> reviews = reviewService.getReviewList(givenProduct1.getId());

        // then
        assertThat(reviews.size()).isEqualTo(1);
        assertThat(reviews.get(0).getReviewContent()).isEqualTo("상품 1번 리뷰");

    }


}