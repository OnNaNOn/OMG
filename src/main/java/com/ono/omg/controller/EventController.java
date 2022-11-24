package com.ono.omg.controller;


import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.response.OrderResponseDto;
import com.ono.omg.dto.response.OrderResponseDto.EventOrderResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.EventService;
import com.ono.omg.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class EventController {
    // 이벤트 상품 주문하기

    private final EventService eventService;

    //동시성 제어주문 Redis > Redisson
    @PostMapping("/{eventId}/confirm")
    public ResponseEntity<ResponseDto<EventOrderResponseDto>> eventOrder(@PathVariable Long eventId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(ResponseDto.success(eventService.eventOrder(eventId, userDetails.getAccount())), HttpStatus.OK);
    }

}
