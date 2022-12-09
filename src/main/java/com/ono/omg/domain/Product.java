package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String title;
    private Integer price;
    private String category;
    private String delivery;
    private Integer stock;
    private String isDeleted;
    private String isSale; // 재고가 0이거나 판매 종료된 상품에 대한 YN 필드
    private String imgUrl;

    @Column(name = "seller")
    private Long sellerId;


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

    public Integer decreaseStock(int stock) {
        if (this.stock - stock < 0) {
            throw new CustomCommonException(ErrorCode.OUT_OF_STOCK);
        }
        this.stock -= stock;

        if(this.stock == 0) {
            this.isSale = "N";
        }
        return this.stock;
    }
}
