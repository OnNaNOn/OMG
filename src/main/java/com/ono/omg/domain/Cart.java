package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, name = "product_id")
    private Long productId;

    /**
     * SJ: isDeleted 로 결정
     */
    @Column(nullable = false)
    private String isDeleted = "N";

    public Cart(Account account, Long productId) {
        this.account = account;
        this.productId = productId;
    }

    /**
     *비즈니스 로직
     */
    public void deleteCart() {
        this.isDeleted = "Y";
    }
}
