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

    /**
     * SJ: 이벤트에 maxParticipant이 있는데 재고 stock과 어떻게 연결 시킬지도 고민 해보면 좋을 것 같음!!
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_title")
    private String eventTitle;

    @Column(name = "event_name")
    private String eventName;

    private String content;

    @Column(name = "max_participant")
    private Long maxParticipant;

    private String isDeleted;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    public Event(String eventTitle, String eventName, String content, Long maxParticipant, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.eventTitle = eventTitle;
        this.eventName = eventName;
        this.content = content;
        this.maxParticipant = maxParticipant;
        this.isDeleted = "N";
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public Long decreaseEventStock(int Eventstock) {
        if (this.maxParticipant - Eventstock < 0) {
            throw new CustomCommonException(ErrorCode.OUT_OF_STOCK);
        }
        this.maxParticipant -= Eventstock;

        if(this.maxParticipant == 0) {
        }
        return this.maxParticipant;
    }
}
