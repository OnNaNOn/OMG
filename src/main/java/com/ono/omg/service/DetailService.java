package com.ono.omg.service;

import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ono.omg.dto.response.ProductResponseDto.DetailProductResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class DetailService {
    private final ProductRepository productRepository;

    public DetailProductResponseDto mainPage(Long productId) {
        return new DetailProductResponseDto(productRepository.detailProduct(productId));
    }
}
