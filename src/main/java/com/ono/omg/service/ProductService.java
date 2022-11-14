package com.ono.omg.service;

import com.ono.omg.controller.ProductTempRepository;
import com.ono.omg.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ono.omg.dto.ProductResponseDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductTempRepository productRepository;

    public List<AllProductInfoResponseDto> findAllSavedProducts() {
        List<Product> products = productRepository.findAll();
        List<AllProductInfoResponseDto> responseDto = new ArrayList<>();

        for (Product product : products) {
            responseDto.add(new AllProductInfoResponseDto(product));
        }
        return responseDto;
    }
}
