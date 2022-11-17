package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "total_price")
    private Integer totalPrice;

    private String isDeleted = "N";


    public Order(Account account, Product product, Integer totalPrice) {
        decrease(product);
        this.account = account;
        this.product = product;
        this.totalPrice = totalPrice;

    }

    public void decrease(Product product) {
        int productStock = product.getStock();
        if (productStock - 1 < 0) {
            /**
             * CustomException 처리로 변경
             */
            throw new RuntimeException("재고부족으로 인해 주문이 불가합니다");
        }
        product.decreaseStock(productStock);
    }
}
