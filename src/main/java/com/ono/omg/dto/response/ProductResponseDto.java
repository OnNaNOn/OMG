package com.ono.omg.dto.response;

import com.ono.omg.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllProductInfoResponseDto {
        // 좋아요 개수
        // 댓글 개수
        // 장바구니

        private Long productId;
        private String title;
        private int price;
        private int stock;
        private String category;
        private String delivery;

        public AllProductInfoResponseDto(Product product) {
            this.productId = product.getId();
            this.title = product.getTitle();
            this.price = product.getPrice();
            this.stock = product.getStock();
            this.category = product.getCategory();
            this.delivery = product.getDelivery();
        }
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllProductManagementResponseDto {
        private Long productId;
        private String title;
        private int price;
        private int stock;
        private String isNowEvent;

        public AllProductManagementResponseDto(Product product) {
            this.productId = product.getId();
            this.title = product.getTitle();
            this.price = product.getPrice();
            this.stock = product.getStock();
            this.isNowEvent = "N";
        }
    }
}
