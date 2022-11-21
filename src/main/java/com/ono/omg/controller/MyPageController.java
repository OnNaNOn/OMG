package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ono.omg.dto.response.OrderResponseDto.CreatedOrdersResponseDto;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final OrderService orderService;

    @GetMapping("/mypage/orders")
    public ResponseDto<List<CreatedOrdersResponseDto>> findAllOrdersParticularAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(orderService.findAllOrders(userDetails.getAccount()));
    }
}
