package com.ono.omg.service;

import com.ono.omg.domain.Product;
import com.ono.omg.domain.Review;
import com.ono.omg.dto.request.ReviewRequestDto;
import com.ono.omg.dto.response.ReviewResponseDto;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.review.ReviewRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {
    /**
     * sy
     * log랑 주석 정리(view 토큰이슈 정리되면 진행 예정)
     * */
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    /**
     * 리뷰 등록
     */
    @Transactional
    //public ReviewResponseDto registerReview(Long productId, UserDetailsImpl userDetails, ReviewRequestDto requestDto) {
    public ReviewResponseDto registerReview(Long productId, ReviewRequestDto requestDto) {
//        String username = userDetails.getAccount().getUsername();
//        Long userId = userDetails.getAccount().getId();
        String reviewContent = requestDto.getReviewContent();

        /**
         * SJ: IllegalArgumentException >> CustomException으로 변경!
         * findReview와 idCheck 메서드도 동일
         */
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("NOT_FOUND_PRODUCT")
        );

//        Review review = new Review(product, reviewContent, userId);
        Review review = new Review(product, reviewContent, 999L);
        reviewRepository.save(review);

        return ReviewResponseDto.builder()
                .productId(productId)
                //.name(username)
                .name("hihihihihi")
                .reviewContent(reviewContent)
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }


    /**
     * 리뷰 수정
     */
    @Transactional
    public Long updateReview(Long reviewId, UserDetailsImpl userDetails, ReviewRequestDto requestDto) {
        Review foundReview = findReview(reviewId);
        Long accountId = userDetails.getAccount().getId();

        //작성자 체크
        idCheck(accountId, foundReview.getId());

        foundReview.setReviewContent(requestDto.getReviewContent());
        return foundReview.getId();
    }


    /**
     * 리뷰 삭제
     */
    @Transactional
//    public Long deleteReview(Long reviewId, UserDetailsImpl userDetails) {
    public Long deleteReview(Long reviewId) {
        Review foundReview = findReview(reviewId);
//        Long accountId = userDetails.getAccount().getId();
//
//        //작성자 체크
//        idCheck(accountId, foundReview.getId());

        //reviewRepository.deleteById(foundReview.getId());

        //N -> Y
        foundReview.deleteReview();
        return foundReview.getId();
    }

    /**
     * 리뷰 조회
     */
    public List<ReviewResponseDto> getReviewList(Long productId) {
        List<ReviewResponseDto> dtoList = new ArrayList<>();
        List<Review> allReview = reviewRepository.findAllByProductId(productId);

        log.info("allReview ===========>" + allReview);
        for (Review review : allReview) {
            String reviewContent = review.getReviewContent();
            Long userId = review.getUserId();
            String username = accountRepository.findUsernameByAccountId(userId);

            log.info("reviewContent ===========>" + reviewContent);
            log.info("userId ===========>" + userId);
            log.info("username ===========>" + username);

            ReviewResponseDto responseDto = ReviewResponseDto.builder()
                    .productId(productId)
                    .reviewContent(reviewContent)
                    .name(username)
                    .createdAt(review.getCreatedAt())
                    .modifiedAt(review.getModifiedAt())
                    .build();

            dtoList.add(responseDto);
        }
        return dtoList;
    }


    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new IllegalArgumentException("NOT_FOUND_REVIEW")
        );
    }

    private void idCheck(Long userId, Long currentId) {
        if (!userId.equals(currentId)) {
            throw new IllegalArgumentException("NOT_WRITER");
        }
    }
}
