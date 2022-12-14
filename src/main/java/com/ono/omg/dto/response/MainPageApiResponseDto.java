package com.ono.omg.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainPageApiResponseDto {
    private Page<SearchResponseDto> products;
    private int nowPage;
    private int startPage;
    private int endPage;
}
