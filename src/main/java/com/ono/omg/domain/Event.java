package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "event_name")
    private String eventName;

    private String content;

    @Column(name = "max_participant")
    private String maxParticipant;

    private String isDeleted;

}
