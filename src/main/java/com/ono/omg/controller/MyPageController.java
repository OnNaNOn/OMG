package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.dto.response.SearchResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyPageController {

    private final OrderService orderService;

    /**
     * 주문내역 조회
     * */
    @GetMapping("/mypage/orders")
    public ResponseDto<List<MainPageOrdersResponseDto>> findAllOrdersParticularAccount(@PageableDefault(size = 10) Pageable pageable,
                                                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(orderService.findAllOrders(pageable, userDetails.getAccount()));
    }
    /**
     * 상품검색
     * */
    //https://dondons.tistory.com/19
    //https://galid1.tistory.com/769

    @GetMapping("/products/search")
    public ResponseDto<Page<SearchResponseDto>> searchOrderInfo(@ModelAttribute SearchRequestDto requestDto, Pageable pageable) {
        return ResponseDto.success(orderService.searchOrders(requestDto, pageable));
    }
}
