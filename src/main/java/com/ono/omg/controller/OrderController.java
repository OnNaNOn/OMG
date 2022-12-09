package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.response.OrderResponseDto.createdOrdersResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

//    private final RedissonClient redissonClient;

    /**
     * 동시성 제어 - Pessimistic Lock
     */
    @PostMapping("/v1/{productId}/confirm")
    public ResponseDto<createdOrdersResponseDto> CreatedOrderWithPessimisticLock(@PathVariable Long productId,
                                                                                 @AuthenticationPrincipal UserDetailsImpl account) {
        log.info("OrderController.CreatedOrderWithPessimisticLock");
        return ResponseDto.success(orderService.productOrderWithPessimisticLock(productId, account.getAccount().getId()));
    }

//    /**
//     * 동시성 제어 - Redisson
//     */
//    @PostMapping("/v2/{productId}/confirm")
//    public ResponseDto<createdOrdersResponseDto> CreatedOrderWithRedisson(@PathVariable Long productId,
//                                                                          @AuthenticationPrincipal UserDetailsImpl account) {
//        return ResponseDto.success(orderService.productOrderRedisson(productId, account.getAccount()));
//    }
}
