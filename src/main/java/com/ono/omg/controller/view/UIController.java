package com.ono.omg.controller.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ono.omg.domain.Product;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.RegistedProductResponseDto;

@Controller
@RequiredArgsConstructor
public class UIController {
    /**
     * SJ: 계층형 아키텍처에 맞게 재고관리와 메인 페이지는
     * Controller > Service > Repository로 변경할 필요 있음
     */
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    // home
    @GetMapping("/omg")
    public String home() {
        return "main/index";
    }

    // 검색 페이지 (기존 상품 페이지)
    @GetMapping("/accounts/signup")
    public String register() {
        return "users/accounts/accountRegisterForm";
    }

    @GetMapping("/accounts/login")
    public String accountsLoginForm() {
        return "users/accounts/accountLoginForm";
    }

    @GetMapping("/admin/login")
    public String adminLoginForm() {
        return "users/accounts/adminLoginForm";
    }

    @GetMapping("/admin/event")
    public String eventRegisterForm() {
        return "event/event";
    }

    @GetMapping("/omg/search")
    public String mainPage(@RequestParam(name = "q", required = false) String query, Model model) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/"));
//        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);

        if(!StringUtils.hasText(query)) {
            return "redirect:/omg";
        }
        model.addAttribute("query", query);
        return "main/mainPage";
    }

    //상세페이지
    @GetMapping("/products/detail/{productId}")
    public String detailProductPage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "detail/detailProduct";
    }
}

