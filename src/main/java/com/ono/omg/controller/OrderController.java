package com.ono.omg.controller;

import com.ono.omg.domain.Account;
import com.ono.omg.dto.common.ResponseDto;
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

import static com.ono.omg.dto.response.OrderResponseDto.CreatedOrdersResponseDto;
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
     * 리팩토링 정상작동 OK
     * 나중에 토큰 구현되면 Account account만 AuthenticationPrincipal로 변경하면 OK
     * 만약 Postman으로 테스트 시 주석 번갈아가면서 테스트
     */
    @PostMapping("/{productId}/confirm")
    public ResponseDto<CreatedOrdersResponseDto> CreatedOrder(@PathVariable Long productId,
                                                              Account account
//                                                              @AuthenticationPrincipal UserDetailsImpl account
                                                              ) {

        RLock lock = redissonClient.getLock(productId.toString());
        CreatedOrdersResponseDto responseDto;
        try {
            boolean available = lock.tryLock(20, 1, TimeUnit.SECONDS);

            if (!available) {
                /**
                 * 별도의 Custom Exception으로 처리
                 */
                return null;
            }
            responseDto = orderService.productOrder(productId, account);
//            responseDto = orderService.productOrder(productId, account.getAccount());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return ResponseDto.success(responseDto);
    }

    /**
     * 주문 취소 (주문취소 및 다른 사용자인 경우 예외처리까지 테스트 완료)
     * (삭제가 아닌 Order.OrderType 을 ORDER_CANCEL 로 변경)
     * API 명세서 추가 필요
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseDto<cancelOrderResponseDto> cancelOrder(@PathVariable Long orderId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(orderService.cancel(orderId, userDetails.getAccount()));
    }
}
