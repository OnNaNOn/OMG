package com.ono.omg.service;

import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ono.omg.dto.response.ProductResponseDto.DetailProductResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetailService {
    private final ProductRepository productRepository;

    public DetailProductResponseDto mainPage(Long productId) {
        return new DetailProductResponseDto(productRepository.detailProduct(productId));
    }
}
