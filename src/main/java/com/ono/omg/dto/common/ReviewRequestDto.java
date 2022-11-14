package com.ono.omg.dto.common;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewRequestDto {
    private String reviewContent;

    @Builder
    public ReviewRequestDto(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
