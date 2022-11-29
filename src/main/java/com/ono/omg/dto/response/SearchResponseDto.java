package com.ono.omg.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class SearchResponseDto {
    private Long productId;
    private String title;
    private Integer price;
    private Integer stock;
    private String category;
    private String delivery;
    private Long sellerId;
    private String isDeleted;
    private String imgUrl;

    @Builder
    @QueryProjection
    public SearchResponseDto(Long productId, String title, Integer price, Integer stock, String category, String delivery, Long sellerId, String isDeleted, String imgUrl) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.delivery = delivery;
        this.sellerId = sellerId;
        this.isDeleted = isDeleted;
        this.imgUrl = imgUrl;
    }
}
