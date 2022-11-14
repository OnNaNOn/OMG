package com.ono.omg.controller;

import com.ono.omg.dto.common.ReviewRequestDto;
import com.ono.omg.dto.common.ReviewResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     */
    @PostMapping("/{productId}/reviews")
    public ReviewResponseDto registerReview(@PathVariable Long productId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails,
                                            ReviewRequestDto requestDto) {
        return reviewService.registerReview(productId, userDetails, requestDto);
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/reviews/{reviewId}")
    public Long updateReview(@PathVariable Long reviewId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails,
                                          ReviewRequestDto requestDto) {
        return reviewService.updateReview(reviewId, userDetails, requestDto);
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/reviews/{reviewId}")
    public Long deleteReview(@PathVariable Long reviewId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.deleteReview(reviewId, userDetails);
    }

    /**
     * 리뷰 조회
     */
    @GetMapping("/{productId}/reviews")
    public List<ReviewResponseDto> getReviewList(@PathVariable Long productId) {
        return reviewService.getReviewList(productId);
    }
}
