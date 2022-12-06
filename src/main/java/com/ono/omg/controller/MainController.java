package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.request.SearchRequestDto;
import com.ono.omg.dto.response.MainPageApiResponseDto;
import com.ono.omg.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final MainService mainService;


    @GetMapping("/api/omg")
    public ResponseDto<MainPageApiResponseDto> home(@RequestParam(name = "q") SearchRequestDto title,
                                                    @RequestParam(name = "page") Integer page) {
        return ResponseDto.success(mainService.home(title.getTitle(), page));
    }

}
