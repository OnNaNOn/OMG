package com.ono.omg.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UIController {
    // home
    @GetMapping("/omg")
    public String home() {
        return "main/index";
    }

    // 검색 페이지 (기존 상품 페이지)
    @GetMapping("/accounts/signup")
    public String register() {
        return "accounts/accountRegisterForm";
    }

    @GetMapping("/accounts/login")
    public String accountsLoginForm() {
        return "accounts/accountLoginForm";
    }

    @GetMapping("/admin/login")
    public String adminLoginForm() {
        return "accounts/adminLoginForm";
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

