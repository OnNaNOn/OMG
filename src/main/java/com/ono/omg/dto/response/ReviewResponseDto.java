package com.ono.omg.dto.response;

import com.ono.omg.domain.Review;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private Long productId;
    private String name;
    private String reviewContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public ReviewResponseDto(Long reviewId, Long productId, String name, String reviewContent, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.name = name;
        this.reviewContent = reviewContent;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
