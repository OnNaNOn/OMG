package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import com.ono.omg.dto.request.ProductReqDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Random;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String delivery;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, name = "seller")
    private Long sellerId;

    @Column(nullable = false)
    private String isDeleted;

    @Column(nullable = false)
    private String imgUrl;

    public Product(Long id, String title, int price, int stock, String category, String delivery, Long sellerId, String isDeleted, String imgUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.delivery = delivery;
        this.sellerId = sellerId;
        this.isDeleted = isDeleted;
        this.imgUrl = imgUrl;
    }

    public Product(Long id, int stock) {
        this.id = id;
        this.stock = stock;
    }

    public Product(ProductReqDto productReqDto, Account account) {
        this.title = productReqDto.getTitle();
        this.price = productReqDto.getPrice();
        this.stock = productReqDto.getStock();
        this.category = productReqDto.getCategory();
        this.delivery = productReqDto.getDelivery();
        this.sellerId = account.getId();
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
    }

    public void isDeleted() {
        this.isDeleted = "Y";
    }

    public void decrease() {
        if (this.stock - 1 < 0) {
            throw new RuntimeException("재고부족으로 인해 주문이 불가합니다");
        }
        this.stock -= 1;

    }
}
