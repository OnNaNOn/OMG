package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.request.ReviewRequestDto;
import com.ono.omg.dto.response.ReviewResponseDto;
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
    public ResponseDto<ReviewResponseDto> registerReview(@PathVariable Long productId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestBody ReviewRequestDto requestDto) {
        return ResponseDto.success(reviewService.registerReview(productId, userDetails, requestDto));
    }

    /**
     * 리뷰 수정
     */
    @PatchMapping("/reviews/{reviewId}")
    public ResponseDto<Long> updateReview(@PathVariable Long reviewId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody ReviewRequestDto requestDto) {
        return ResponseDto.success(reviewService.updateReview(reviewId, userDetails, requestDto));
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseDto<Long> deleteReview(@PathVariable Long reviewId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(reviewService.deleteReview(reviewId, userDetails));
    }

    /**
     * 리뷰 조회
     */
    @GetMapping("/{productId}/reviews")
    public ResponseDto<List<ReviewResponseDto>> getReviewList(@PathVariable Long productId) {
        return ResponseDto.success(reviewService.getReviewList(productId));
    }
}
