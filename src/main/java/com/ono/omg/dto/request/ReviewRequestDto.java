package com.ono.omg.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewRequestDto {
    private String reviewContent;

    @Builder
    public ReviewRequestDto(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
