package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.response.CartResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class CartController {
    private final CartService cartService;

    /**
     * 상품 장바구니 담기
     */
    @PostMapping("/{productId}/cart")
    public ResponseDto<Long> inputProduct(@PathVariable Long productId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails
                                          ) {
        return ResponseDto.success(cartService.inputProduct(productId, userDetails));
    }

    /**
     * 장바구니 조회
     * */
    @GetMapping("/cart")
    public ResponseDto<List<CartResponseDto>> getCartList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseDto.success(cartService.getCartList(userDetails));
    }



}
