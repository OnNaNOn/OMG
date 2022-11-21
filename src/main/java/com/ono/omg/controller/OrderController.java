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
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseDto<Long> CreatedOrder(@PathVariable Long productId,
//                                                              Account account
                                                              @AuthenticationPrincipal UserDetailsImpl account
                                                              ) {

        RLock lock = redissonClient.getLock(productId.toString());
        Long orderId;
        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!available) {
                /**
                 * SJ: 별도의 Custom Exception으로 처리
                 */
                return null;
            }
//            responseDto = orderService.productOrder(productId, account);
            orderId = orderService.productOrder(productId, account.getAccount());
        } catch (InterruptedException e) {
            /**
             * SJ: 별도의 Custom Exception으로 처리
             */
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return ResponseDto.success(orderId);
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
//    * */
//    @GetMapping("/orders")
//    public ResponseDto<List<MyPageResponseDto>> getOrderList(@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return ResponseDto.success(orderService.getOrderList(userDetails));
//    }

}
