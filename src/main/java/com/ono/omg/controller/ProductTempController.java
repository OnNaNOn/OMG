package com.ono.omg.controller;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.ProductResponseDto;
import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.ono.omg.dto.ProductResponseDto.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductTempController {

    private final ProductService productService;
    private final ProductTempRepository productRepository;


    @GetMapping("/omg")
    public ResponseDto<List<AllProductInfoResponseDto>> findAllProducts() {
        return ResponseDto.success(productService.findAllSavedProducts());
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 10; i++) {
            productRepository.save(new Product("title" + i, 1000, 100, "음식", "빠른 배송"));
        }
    }
}
