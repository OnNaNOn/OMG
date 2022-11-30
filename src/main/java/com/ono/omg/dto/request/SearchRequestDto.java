package com.ono.omg.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SearchRequestDto {
    private String title;
    private Integer price;
    private Integer stock;
    private String category;
    private String delivery;
    private Long sellerId;
    private String isDeleted;
    private String imgUrl;

    public void setTitle(String title) {
        this.title = title;
    }

    public SearchRequestDto(String title) {
        this.title = title;
    }
}
