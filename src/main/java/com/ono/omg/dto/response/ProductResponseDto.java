package com.ono.omg.dto.response;

import com.ono.omg.domain.Product;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductResponseDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ProductResDto{
        private String title;
        private Integer price;
        private Integer stock;
        private String category;
        private String delivery;
        private Long sellerId;
        private String isDeleted;
        private String imgUrl;

        public ProductResDto(Product product) {
            this.title = product.getTitle();
            this.price = product.getPrice();
            this.stock = product.getStock();
            this.category = product.getCategory();
            this.delivery = product.getDelivery();
            this.sellerId = product.getSellerId();
            this.isDeleted = product.getIsDeleted();
            this.imgUrl = product.getImgUrl();
        }
    }

    /**
     * 메인 페이지 상품 정보 DTO
     */
    @Getter
    @NoArgsConstructor
    @ToString
    public static class MainPageResponseDto {
        private Long productId;
        private String title;
        private Integer price;
        private String category;
        private String delivery;
        private Integer stock;

        @QueryProjection
        public MainPageResponseDto(Long productId, String title, Integer price, String category, String delivery, Integer stock) {
            this.productId = productId;
            this.title = title;
            this.price = price;
            this.category = category;
            this.delivery = delivery;
            this.stock = stock;
        }
    }

    /**
     * ===== 테스트용 =====
     * 등록된 모든 상품에 대한 상품명, 이미지를 읽어오는 DTO
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailProductResponseDto {
        private Long productId;
        private String imgUrl;
        private String title;
        private Integer price;
        private String category;
        private String delivery;
        private Integer stock;

        public DetailProductResponseDto(Product product) {
            this.productId = product.getId();
            this.imgUrl = product.getImgUrl();
            this.title = product.getTitle();
            this.price = product.getPrice();
            this.category = product.getCategory();
            this.delivery = product.getDelivery();
            this.stock = product.getStock();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistedProductResponseDto {
        private String title;
        private String imgUrl;

        public RegistedProductResponseDto(Product product) {
            this.title = product.getTitle();
            this.imgUrl = product.getImgUrl();
        }
    }
}
