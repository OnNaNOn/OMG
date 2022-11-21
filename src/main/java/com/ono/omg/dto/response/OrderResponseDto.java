package com.ono.omg.dto.response;

import com.ono.omg.domain.Product;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderResponseDto {

    @Getter
    @NoArgsConstructor
    public static class CreatedOrdersResponseDto {

        private String imgUrl;
        private String title;


        @QueryProjection
        public CreatedOrdersResponseDto(String imgUrl, String title) {
            this.imgUrl = imgUrl;
            this.title = title;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class cancelOrderResponseDto {
        /**
         * 다중 상품인 경우 아래 두 필드는 List에 별도의 Dto로 담김
         */
        private Long productId;
        private String productName;

        private String orderStatus;
    }
}
