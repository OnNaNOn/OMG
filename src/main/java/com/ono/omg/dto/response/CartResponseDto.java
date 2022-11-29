package com.ono.omg.dto.response;

import lombok.*;

@NoArgsConstructor
@Getter
public class CartResponseDto {
    public Long productId;
    public String title;
    public int price;
    public String category;
    public String delivery;
    public int stock;
    public Long seller;
    public String imgUrl;

    @Builder
    public CartResponseDto(Long productId, String title, int price, String category, String delivery, int stock, Long seller, String imgUrl) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.category = category;
        this.delivery = delivery;
        this.stock = stock;
        this.seller = seller;
        this.imgUrl = imgUrl;
    }
}
