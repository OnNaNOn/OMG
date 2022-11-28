package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    private String eventTitle;

    private String content;

    private Integer productPrice;

    @Column(name = "max_participant")
    private Long maxParticipant; // Max Participant == Product Stock

    private String isDeleted;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    public Event(Long productId, String eventTitle, String content, Integer productPrice, Long maxParticipant, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.productId = productId;
        this.eventTitle = eventTitle;
        this.content = content;
        this.productPrice = productPrice;
        this.maxParticipant = maxParticipant;
        this.isDeleted = "N";
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public Long decreaseEventStock(int eventStock) {
        if (this.maxParticipant - eventStock < 0) {
            throw new CustomCommonException(ErrorCode.OUT_OF_STOCK);
        }
        this.maxParticipant -= eventStock;

        if(this.maxParticipant == 0) {
        }
        return this.maxParticipant;
    }
}
