package com.ono.omg.controller;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.MainPageApiResponseDto;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainPageController {
    private final ProductRepository productRepository;

    @GetMapping("/api/omg")
    public MainPageApiResponseDto mainPage(@RequestParam(name = "page") Integer page) {
        PageRequest pageable = PageRequest.of(page, 10);

        Page<Product> products = productRepository.findAll(pageable);
        int totalPages = products.getTotalPages();
        long totalElements = products.getTotalElements();

        int nowPage = products.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 2, 1);
        int endPage = Math.min(nowPage + 2, products.getTotalPages());

        return new MainPageApiResponseDto(products, totalPages, totalElements, nowPage, startPage, endPage);
    }
}
