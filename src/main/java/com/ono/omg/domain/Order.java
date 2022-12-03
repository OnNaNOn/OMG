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

    @Enumerated(EnumType.STRING)
    private OrderType orderType = OrderType.ORDER_OK;

    private Long cartId;

    private Long eventId;

    /**
     * 일반 상품 주문
     */
    public Order(Account account, Product product, Integer totalPrice) {
        this.account = account;
        this.product = product;
        this.totalPrice = totalPrice;
    }

    /**
     * 이벤트 상품 주문
     */
    public Order(Account findAccount, Product product, Long eventId, int productPrice) {
        this.account = findAccount;
        this.eventId = eventId;
        this.product = product;
        this.totalPrice = productPrice;
    }

    /**
     * 주문 취소
     */
    public void orderCancel() {
        this.orderType = OrderType.ORDER_CANCEL;
    }
}
