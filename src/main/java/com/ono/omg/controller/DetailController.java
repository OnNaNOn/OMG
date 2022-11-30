package com.ono.omg.controller;

import com.ono.omg.dto.response.ProductResponseDto.DetailProductResponseDto;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DetailController {
    private final ProductRepository productRepository;

    @GetMapping("/api/products/detail/{productId}")
    public DetailProductResponseDto mainPage(@PathVariable Long productId) {
        return new DetailProductResponseDto(productRepository.detailProduct(productId));
    }
}
