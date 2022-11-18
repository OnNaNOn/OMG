package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.*;
import static com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/omg")
    public ResponseDto<List<MainPageResponseDto>> findAllProducts() {
        return ResponseDto.success(productService.findAllSavedProducts());
    }

    // 상품등록
    @PostMapping("/products")
    public ResponseEntity<ResponseDto<String>> createProduct(@RequestBody ProductReqDto productReqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(ResponseDto.success(productService.createProduct(productReqDto, userDetails.getAccount())), HttpStatus.OK);
    }

    // 상품수정
    @PatchMapping("/products/{productId}")
    public ResponseEntity<ResponseDto<String>> updateProduct(@PathVariable Long productId, @RequestBody ProductReqDto productReqDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(ResponseDto.success(productService.updateProduct(productId, productReqDto, userDetails.getAccount())), HttpStatus.OK);
    }

    //상품삭제
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ResponseDto<String>> deleteProduct(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(ResponseDto.success(productService.deleteProduct(productId, userDetails.getAccount())), HttpStatus.OK);
    }

    //상품조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<ResponseDto<ProductResDto>> searchProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(ResponseDto.success(productService.searchProduct(productId)), HttpStatus.OK);
    }
}
