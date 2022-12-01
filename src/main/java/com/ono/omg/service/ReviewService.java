package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.domain.Review;
import com.ono.omg.dto.request.ReviewRequestDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.dto.response.ReviewResponseDto;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.review.ReviewRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    /**
     * 리뷰 등록
     */
    @Transactional
    public ReviewResponseDto registerReview(Long productId, Account account, ReviewRequestDto requestDto) {
        String username = account.getUsername();
        Long userId = account.getId();
        String reviewContent = requestDto.getReviewContent();

        /**
         * SJ: IllegalArgumentException >> CustomException으로 변경!
         * findReview와 idCheck 메서드도 동일
         */
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("NOT_FOUND_PRODUCT")
        );

        Review review = new Review(product, reviewContent, userId);
        reviewRepository.save(review);

        return ReviewResponseDto.builder()
                .productId(productId)
                .name(username)
                .reviewContent(reviewContent)
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }


    /**
     * 리뷰 수정 (SJ: 반환 값을 변경했으면 좋겠습니다..!)
     */
    @Transactional
    public Long updateReview(Long reviewId, Account account, ReviewRequestDto requestDto) {
        Review foundReview = findReview(reviewId);
        Long accountId = account.getId();

        //작성자 체크
        idCheck(accountId, foundReview.getId());

        foundReview.setReviewContent(requestDto.getReviewContent());
        return foundReview.getId();
    }


    /**
     * 리뷰 삭제 (SJ: 반환 값을 변경했으면 좋겠습니다..!)
     */
    @Transactional
    public Long deleteReview(Long reviewId, Account account) {
        Review foundReview = findReview(reviewId);
        Long accountId = account.getId();

        //작성자 체크
        idCheck(accountId, foundReview.getId());

        reviewRepository.deleteById(foundReview.getId());

        //N -> Y
        foundReview.deleteReview();
        return foundReview.getId();
    }

    /**
     * 리뷰 조회
     */
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewList(Long productId) {
        List<ReviewResponseDto> dtoList = new ArrayList<>();
        List<Review> allReview = reviewRepository.findAllByProductId(productId);

        for (Review review : allReview) {
            String reviewContent = review.getReviewContent();
            Long userId = review.getUserId();
            String username = accountRepository.findUsernameByAccountId(userId);

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

    /**
     * 리뷰 등록내역 조회
     * */
    @Transactional(readOnly = true)
    public List<MainPageOrdersResponseDto> reviewDetails(UserDetailsImpl userDetails) {
        Long accountId = userDetails.getAccount().getId();
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "modifiedAt");
        List<Review> registerReviewList = reviewRepository.findReviewList(pageable, accountId);

        System.out.println("accountId" + accountId);
        for (Review review : registerReviewList) {
            System.out.println(review.getUserId());
        }

        return registerReviewList.stream()
                .map((o)-> new MainPageOrdersResponseDto(o.getId(), o.getProduct().getImgUrl(), o.getProduct().getTitle()))
                .collect(toList());
    }
}
