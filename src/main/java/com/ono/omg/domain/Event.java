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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    private String eventTitle;
    private String content;

    private String isDeleted;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public Event(Long productId, String eventTitle, String content, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.productId = productId;
        this.eventTitle = eventTitle;
        this.content = content;
        this.isDeleted = "N";
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
