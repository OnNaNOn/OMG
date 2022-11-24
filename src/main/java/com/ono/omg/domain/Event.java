package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    /**
     * SJ: 이벤트에 maxParticipant이 있는데 재고 stock과 어떻게 연결 시킬지도 고민 해보면 좋을 것 같음!!
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "event_name")
    private String eventName;

    private String content;

    @Column(name = "max_participant")
    private String maxParticipant;

    private String isDeleted;

    private LocalDateTime createdAt;

    private LocalDateTime endedAt;

    public Event(Long productId, String eventName, String content, String maxParticipant, String isDeleted, LocalDateTime createdAt, LocalDateTime endedAt) {
        this.productId = productId;
        this.eventName = eventName;
        this.content = content;
        this.maxParticipant = maxParticipant;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
    }
}
