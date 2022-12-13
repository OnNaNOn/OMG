package com.ono.omg.repository.product;

import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;

public interface ProductRepositoryCustom {
    Page<MainPageResponseDto> findAllProductStock(String title, Pageable pageable);

    Page<SearchResponseDto> searchProduct(SearchRequestDto requestDto, Pageable pageable);

    Page<SearchResponseDto> searchProductUsedFullTextSearch(SearchRequestDto requestDto, Pageable pageable);

    Page<SearchResponseDto> searchProductUsedFullTextSearchAndCoveringIndex(String title, Pageable pageable);

    List<SearchResponseDto> searchProductUsedNoOffset(Long productId, String title, Integer pageSize);
}