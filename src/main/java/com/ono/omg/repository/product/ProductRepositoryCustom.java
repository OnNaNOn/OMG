package com.ono.omg.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;

public interface ProductRepositoryCustom {
    // 저장하기 버튼 >> API 생성

    // 상품 번호
    // 상품 명
    // 재고 현황
    // 판매 유무
    // 이벤트 유무
    Page<AllProductManagementResponseDto> findAllProductStock(Pageable pageable);

}