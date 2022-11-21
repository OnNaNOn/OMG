package com.ono.omg.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDto {
    private Page<AllProductManagementResponseDto> products;
    private int totalPages;
    private long totalElements;

    private int nowPage;
    private int startPage;
    private int endPage;
}
