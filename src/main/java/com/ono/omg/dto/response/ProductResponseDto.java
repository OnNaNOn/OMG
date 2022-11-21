package com.ono.omg.dto.response;

import com.ono.omg.domain.Product;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
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
        private int price;
        private int stock;
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
     * 관리자 전용 상품 관리 페이지 DTO
     */
    @Getter
    @NoArgsConstructor
    @ToString
    public static class AllProductManagementResponseDto {
        private Long productId;           // 상품 번호
        private String title;             // 상품 명
        private int price;                // 상품 가격
        private int stock;                // 재고 현황
        private String isSale = isSold(); // 판매 유무
        private String isNowEvent = "N";  // 이벤트 유무

        private String isSold() {
            if(stock < 0) {
                /**
                 * 정합성이 깨질 경우 500번 에러와 함께 재고 수량 불일치 메시지 발생
                 */
                throw new CustomCommonException(ErrorCode.NO_CONSISTENCY_STOCK);
            }

            if(stock == 0) {
                return "N";
            }
            return "Y";
        }

        @QueryProjection
        public AllProductManagementResponseDto(Long productId, String title, int price, int stock) {
            this.productId = productId;
            this.title = title;
            this.price = price;
            this.stock = stock;
        }
    }

    /**
     * 메인 페이지 상품 정보 DTO
     */
    @Getter
    @NoArgsConstructor
    @ToString
    @AllArgsConstructor
    public static class MainPageResponseDto {
        private Long productId;
        private String title;
        private int price;
        private String category;
        private String delivery;
        private int stock;

        public MainPageResponseDto(Product product) {
            this.productId = product.getId();
            this.title = product.getTitle();
            this.price = product.getPrice();
            this.category = product.getCategory();
            this.delivery = product.getDelivery();
            this.stock = product.getStock();
        }
    }

    /**
     * ===== 테스트용 =====
     * 등록된 모든 상품에 대한 상품명, 이미지를 읽어오는 DTO
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class detailProductResponseDto {
        private Long productId;
        private String imgUrl;
        private String title;
        private int price;
        private String category;
        private String delivery;
        private int stock;

        public detailProductResponseDto(Product product) {
            this.productId = product.getId();
            this.imgUrl = product.getImgUrl();
            this.title = product.getTitle();
            this.price = product.getPrice();
            this.category = product.getCategory();
            this.delivery = product.getDelivery();
            this.stock = product.getStock();

            log.info("product.getImgUrl() = {}", product.getImgUrl());
        }
    }

    @ToString
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
