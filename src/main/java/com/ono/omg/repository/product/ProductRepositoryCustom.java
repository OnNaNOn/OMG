package com.ono.omg.repository.product;

import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;

public interface ProductRepositoryCustom {
    Page<AllProductManagementResponseDto> findAllProductStock(Pageable pageable);

    Page<SearchResponseDto> searchProduct(SearchRequestDto requestDto, Pageable pageable);

    Page<SearchResponseDto> searchProductUsedFullTextSearch(SearchRequestDto requestDto, Pageable pageable);

    Page<SearchResponseDto> searchProductUsedFullTextSearchAndRowLookup(SearchRequestDto requestDto, Pageable pageable);
}