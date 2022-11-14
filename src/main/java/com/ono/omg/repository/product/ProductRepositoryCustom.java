package com.ono.omg.repository.product;

import java.util.List;

import static com.ono.omg.dto.ProductResponseDto.AllProductInfoResponseDto;

public interface ProductRepositoryCustom {

    List<AllProductInfoResponseDto> findAllProducts();
}
