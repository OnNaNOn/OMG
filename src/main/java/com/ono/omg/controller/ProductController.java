package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    // 상품등록
    @PostMapping
    public ResponseEntity<ResponseDto<String>> createProduct(@RequestBody ProductReqDto productReqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(ResponseDto.success(productService.createProduct(productReqDto, userDetails.getAccount())), HttpStatus.OK);
    }

    //상품조회
    @GetMapping("/{productId}")
    public ResponseEntity<ResponseDto<ProductResDto>> searchProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(ResponseDto.success(productService.searchProduct(productId)), HttpStatus.OK);
    }

    // 상품 상세페이지 조회
    @GetMapping("/detail/{productId}")
    public ResponseDto<DetailProductResponseDto> detailPage(@PathVariable Long productId) {
        return ResponseDto.success(productService.mainPage(productId));
    }

}
