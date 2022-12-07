package com.ono.omg.dto.response;

import com.ono.omg.domain.Event;
import com.ono.omg.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class EventResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllEventResponse {
        private Long productId;
        private Long eventId;
        private String eventName;
        private String productName;
        private String content;
        private Integer productPrice;
        private Integer stock;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;

        public AllEventResponse(Event event, Product product) {
            this.productId = product.getId();
            this.eventId = event.getId();
            this.eventName = event.getEventTitle();
            this.productName = product.getTitle();
            this.content = event.getContent();
            this.productPrice = product.getPrice();
            this.stock = product.getStock();
            this.startedAt = event.getStartedAt();
            this.endedAt = event.getEndedAt();
        }
    }
}
