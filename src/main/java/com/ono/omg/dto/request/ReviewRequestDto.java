package com.ono.omg.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewRequestDto {
    private String reviewContent;

    @Builder
    public ReviewRequestDto(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
