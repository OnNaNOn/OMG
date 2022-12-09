package com.ono.omg.dto.response;

import com.ono.omg.domain.Product;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderResponseDto {

    @Getter
    @NoArgsConstructor
    public static class MainPageOrdersResponseDto {
        private Long productId;
        private String imgUrl;
        private String title;

        @QueryProjection
        public MainPageOrdersResponseDto(Long productId, String imgUrl, String title) {
            this.productId = productId;
            this.imgUrl = imgUrl;
            this.title = title;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventOrderResponseDto {
        private Long orderId;
        private Long eventId;
        private String username;
        private String eventTitle;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createdOrdersResponseDto {
        private Long orderId;
        private Long productId;

        private Integer getTotalPrice;
        private String username;
        private Integer stock;

        private Integer price;
        private String category;
        private String delivery;
        private String imgUrl;

        public createdOrdersResponseDto(Long orderId, Integer getTotalPrice, String username, Product product) {
            this.orderId = orderId;
            this.getTotalPrice = getTotalPrice;
            this.username = username;
            this.stock = product.getStock();
            this.productId = product.getId();
            this.price = product.getPrice();
            this.category = product.getCategory();
            this.delivery = product.getDelivery();
            this.imgUrl = product.getImgUrl();
        }
    }
}
