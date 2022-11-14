package com.ono.omg.controller;


import com.ono.omg.domain.Product;
import com.ono.omg.dto.common.ProductReqDto;
import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ResponseDto<String>> createProduct(@RequestBody ProductReqDto productReqDto) {
        return new ResponseEntity<>(ResponseDto.success(productService.createProduct(productReqDto)),HttpStatus.CREATED);
    }

}
