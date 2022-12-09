package com.ono.omg.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class EventRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateEventDto {
        private String eventName;
        private String eventDesc;
        private String productName;
        private Integer productPrice;
        private Integer productStock;
        private LocalDateTime startDate;
    }

}
