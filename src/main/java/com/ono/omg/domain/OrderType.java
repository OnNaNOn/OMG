package com.ono.omg.domain;

public enum OrderType {

    ORDER_OK("주문 완료"),
    DELIVERY_READY("상품 준비"),
    DELIVERY_START("배송 시작"),
    RECEIVE_OK("수령 완료");

    private String status;

    private OrderType() {}

    OrderType(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
