package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, name = "review_content")
    private String reviewContent;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String isDeleted;

    public Review(Product product, String reviewContent, Long userId) {
        this.product = product;
        this.reviewContent = reviewContent;
        this.userId = userId;
        this.isDeleted = "N";
    }


    /**
     *비즈니스 로직
     */
    public void setReviewContent(String reviewContent){
        this.reviewContent = reviewContent;
    }

    public void deleteReview() {
        this.isDeleted = "Y";
    }
}
