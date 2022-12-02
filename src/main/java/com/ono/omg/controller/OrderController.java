package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.response.OrderResponseDto.createdOrdersResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static com.ono.omg.dto.response.OrderResponseDto.cancelOrderResponseDto;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    // 주문하기
    // 특정 사용자의 주문 목록 조회

    private final OrderService orderService;
    private final RedissonClient redissonClient;

    /**
     * 동시성 제어 With Redis - Redisson >>>>>> 사용 X <<<<<<<
     */
    @PostMapping("/{productId}/confirm")
    public ResponseDto<createdOrdersResponseDto> CreatedOrder(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl account) {

        RLock lock = redissonClient.getLock(productId.toString());
        createdOrdersResponseDto createdOrdersResponseDto;
        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!available) {
                /**
                 * SJ: 별도의 Custom Exception으로 처리
                 */
                return null;
            }
//            responseDto = orderService.productOrder(productId, account);
            createdOrdersResponseDto = orderService.productOrder(productId, account.getAccount());
        } catch (InterruptedException e) {
            /**
             * SJ: 별도의 Custom Exception으로 처리
             */
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return ResponseDto.success(createdOrdersResponseDto);
    }

    /**
     * 동시성 제어 With Pessimistic Lock
     */
    @PostMapping("/v1/{productId}/confirm")
    public ResponseDto<createdOrdersResponseDto> CreatedOrderWithPessimisticLock(@PathVariable Long productId,
                                                                                 @AuthenticationPrincipal UserDetailsImpl account) {
        return ResponseDto.success(orderService.productOrderWithPessimisticLock(productId, account.getAccount().getId()));
    }

    @PostMapping("/v2/{productId}/confirm")
    public ResponseDto<createdOrdersResponseDto> CreatedOrderWithRedisson(@PathVariable Long productId,
                                                                          @AuthenticationPrincipal UserDetailsImpl account) {
        return ResponseDto.success(orderService.productOrderRedisson(productId, account.getAccount()));
    }

    /**
     * 주문 취소
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseDto<cancelOrderResponseDto> cancelOrder(@PathVariable Long orderId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(orderService.cancel(orderId, userDetails.getAccount()));
    }

//    /**
//     * 주문내역 조회
//    */
//    @GetMapping("/orders")
//    public ResponseDto<List<MyPageResponseDto>> getOrderList(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return ResponseDto.success(orderService.getOrderList(userDetails));
//    }
}
