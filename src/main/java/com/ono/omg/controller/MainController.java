package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.MainPageApiResponseDto;
import com.ono.omg.dto.response.SearchResponseDto;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MainController {
    private final MainService mainService;

    @GetMapping("/api/v1/omg")
    public ResponseDto<MainPageApiResponseDto> home(@RequestParam(name = "q", required = false) SearchRequestDto title,
                                                    @RequestParam(name = "page", required = false) Integer page) {
        long startTime = System.currentTimeMillis();

        ResponseDto<MainPageApiResponseDto> responseDto = ResponseDto.success(mainService.home(title.getTitle(), page));

        long stopTime = System.currentTimeMillis();
        log.info("exec time = {}", stopTime - startTime);

        return responseDto;
    }

    @GetMapping("/api/v2/omg")
    public List<SearchResponseDto> searchProducts(@RequestParam(name = "start", required = false) Long start,
                                             @RequestParam(name = "q", required = false) SearchRequestDto title,
                                             @PageableDefault(size = 15) Pageable pageable) {
        long startTime = System.currentTimeMillis();

        List<SearchResponseDto> responseDto = mainService.searchProductUsedFullTextSearchAndNoOffset(start, title.getTitle(), pageable.getPageSize());

        long stopTime = System.currentTimeMillis();
        log.info("검색어 = {}, 실행 시간 = {}", title.getTitle(), stopTime - startTime + "ms");
        return responseDto;
    }
}
