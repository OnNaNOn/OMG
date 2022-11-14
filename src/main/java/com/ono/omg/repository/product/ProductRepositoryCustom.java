package com.ono.omg.repository.product;

import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductInfoResponseDto;

public interface ProductRepositoryCustom {

    List<AllProductInfoResponseDto> findAllProducts();
}
