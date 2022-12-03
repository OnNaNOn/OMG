package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.response.ProductResponseDto.DetailProductResponseDto;
import com.ono.omg.service.DetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DetailController {
    private final DetailService detailService;

    public DetailController(DetailService detailService) {
        this.detailService = detailService;
    }

    @GetMapping("/api/products/detail/{productId}")
    public ResponseDto<DetailProductResponseDto> detailPage(@PathVariable Long productId) {
        return ResponseDto.success(detailService.mainPage(productId));
    }
}
