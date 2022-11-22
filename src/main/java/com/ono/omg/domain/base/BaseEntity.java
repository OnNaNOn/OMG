package com.ono.omg.domain.base;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    // Entity가 생성되어 저장될 때 시간이 자동 저장
    @CreatedDate
    @Column(name = "created_at", columnDefinition = "DATETIME(6)", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // 조회한 Entity 값을 변경할 때 시간이 자동 저장
    @LastModifiedDate
    @Column(name = "modified_at", columnDefinition = "DATETIME(6)", nullable = false)
    private LocalDateTime modifiedAt;
}
