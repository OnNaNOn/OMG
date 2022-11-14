package com.ono.omg.dto.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {
    private Long productId;
    private String name;
    private String reviewContent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public ReviewResponseDto(Long productId, String name, String reviewContent, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.productId = productId;
        this.name = name;
        this.reviewContent = reviewContent;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
