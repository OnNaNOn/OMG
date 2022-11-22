package com.ono.omg.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;

public interface ProductRepositoryCustom {
    Page<AllProductManagementResponseDto> findAllProductStock(Pageable pageable);

}