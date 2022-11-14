package com.ono.omg.controller;

import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class CartController {
    private final CartService cartService;

    /**
     * 상품 장바구니 담기
     */
    @PostMapping("/{productId}/cart")
    public Long inputProduct(@PathVariable Long productId,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return cartService.inputProduct(productId, userDetails);
    }

}
