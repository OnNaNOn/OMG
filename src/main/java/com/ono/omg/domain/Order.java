package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * SJ: 장바구니 ID
     */
//    private Long cartId;

    public Order(Account account, Product product, Integer totalPrice) {
//        decrease(product);
        this.account = account;
        this.product = product;
        this.totalPrice = totalPrice;
    }

    /**
     * 재고 감소 (재고 없을 시 예외 발생)
     */
    public void decrease(Product product) {
        int productStock = product.getStock();
        if (productStock - 1 < 0) {
            throw new CustomCommonException(ErrorCode.OUT_OF_STOCK);
        }
        product.decreaseStock(productStock);
    }

    /**
     * 주문 취소
     */
    public void orderCancel() {
        this.orderType = OrderType.ORDER_CANCEL;
    }
}
