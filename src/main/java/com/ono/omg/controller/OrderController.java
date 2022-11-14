package com.ono.omg.controller;

import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    // 주문하기
    // 특정 사용자의 주문 목록 조회

    private final OrderService orderService;

    @PostMapping("/{productId}/confirm")
    public void CreatedOrder(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        orderService.productOrder(productId, userDetails.getAccount());
    }
}
