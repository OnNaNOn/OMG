package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import com.ono.omg.dto.request.ProductReqDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public Product(String title, int price, int stock, String category, String delivery, Long sellerId, String isDeleted, String imgUrl) {
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.delivery = delivery;
        this.sellerId = sellerId;
        this.isDeleted = isDeleted;
        this.imgUrl = imgUrl;
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
}
