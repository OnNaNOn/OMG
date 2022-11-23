package com.ono.omg.dto.response;

import com.ono.omg.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainPageApiResponseDto {
    private Page<Product> products;

    private int totalPages;
    private long totalElements;

    private int nowPage;
    private int startPage;
    private int endPage;
}
