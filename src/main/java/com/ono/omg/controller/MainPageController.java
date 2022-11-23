package com.ono.omg.controller;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.MainPageApiResponseDto;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainPageController {
    private final ProductRepository productRepository;

    /*
     * UserDetails 추가 해야할듯, api명도 수정필요(uicontroller에 있는 거랑 충돌)
     * */
    @GetMapping("/api/omg")
    public MainPageApiResponseDto mainPage(@RequestParam(name = "page") Integer page) {
        PageRequest pageable = PageRequest.of(page, 10);

        Page<Product> products = productRepository.findAll(pageable);
        int totalPages = products.getTotalPages();
        long totalElements = products.getTotalElements();

//        List<MainPageResponseDto> responseDto = new ArrayList<>();
//        for (Product product : products) {
//            responseDto.add(new MainPageResponseDto(product));
//        }

        //페이지블럭 처리
        //1을 더해주는 이유는 pageable은 0부터라 1을 처리하려면 1을 더해서 시작해주어야 한다.
        int nowPage = products.getPageable().getPageNumber() + 1;
        //-1값이 들어가는 것을 막기 위해서 max값으로 두 개의 값을 넣고 더 큰 값을 넣어주게 된다.
        int startPage = Math.max(nowPage - 2, 1);
        int endPage = Math.min(nowPage + 2, products.getTotalPages());

//        System.out.println("@@@@@@@@@@@@@@@@@" + products);
        return new MainPageApiResponseDto(products, totalPages, totalElements, nowPage, startPage, endPage);
    }


}
