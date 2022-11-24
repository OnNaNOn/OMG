package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    /**
     * SJ: entity에 nullable에 대한 항목을 추가하는 것이 좋을 것 같음
     * 처음 알았는데 필드에 nullable = false이면 테이블이 생성될 때 Not Null이 됨
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String delivery;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, name = "seller")
    private Long sellerId;

    @Column(nullable = false)
    private String isDeleted;

    // 재고가 0이거나 판매 종료된 상품에 대한 YN 필드
    private String isSale;

    @Column(nullable = false)
    private String imgUrl;

    public Product(ProductReqDto productReqDto, Account account) {
        this.title = productReqDto.getTitle();
        this.price = productReqDto.getPrice();
        this.stock = productReqDto.getStock();
        this.category = productReqDto.getCategory();
        this.delivery = productReqDto.getDelivery();
        this.sellerId = account.getId();
        this.imgUrl = productReqDto.getImgUrl();
        this.isDeleted = "N";
    }

    public Product(String title, int price, String category, String delivery, int stock, Long sellerId) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.delivery = delivery;
        this.stock = stock;
        this.sellerId = sellerId;
        this.isDeleted = "N";
        this.isSale = "Y";
        this.imgUrl = "https://jaesa-bucket.s3.ap-northeast-2.amazonaws.com/SpartaIcon" + numRandom() + ".png";
    }

    private String numRandom() {
        String rand = String.valueOf((int) (Math.random() * 12) + 1);

        if (rand.length() == 1) {
            rand = "0" + rand;
        }
        return rand;
    }


    public void updateProduct(ProductReqDto productReqDto) {
        this.title = productReqDto.getTitle();
        this.price = productReqDto.getPrice();
        this.stock = productReqDto.getStock();
        this.category = productReqDto.getCategory();
        this.delivery = productReqDto.getDelivery();
        this.imgUrl = productReqDto.getImgUrl();
    }

    public void decreaseStock(int stock) {
        if (this.stock - stock < 0) {
            throw new CustomCommonException(ErrorCode.OUT_OF_STOCK);
        }
        this.stock -= stock;

        if(this.stock == 0) {
            this.isSale = "N";
        }
    }

    public void isDeleted() {
        this.isDeleted = "Y";
    }
}
