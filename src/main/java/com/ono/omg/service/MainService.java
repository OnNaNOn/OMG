package com.ono.omg.service;

import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.MainPageApiResponseDto;
import com.ono.omg.dto.response.SearchResponseDto;
import com.ono.omg.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MainService {
    private final ProductRepository productRepository;

    public MainService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MainPageApiResponseDto home(String title, Integer page) {
        PageRequest pageable = PageRequest.of(page, 10);

        return toMainPageApiResponseDto(productRepository.searchProductUsedFullTextSearchAndCoveringIndex(title, pageable));
    }

    private MainPageApiResponseDto toMainPageApiResponseDto(Page<SearchResponseDto> products) {
        int totalPages = products.getTotalPages();
        long totalElements = products.getTotalElements();

        int nowPage = products.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 2, 1);
        int endPage = Math.min(nowPage + 2, products.getTotalPages());

        return new MainPageApiResponseDto(products, totalPages, totalElements, nowPage, startPage, endPage);
    }
}
