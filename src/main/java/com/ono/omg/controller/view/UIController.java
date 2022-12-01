package com.ono.omg.controller.view;

import com.ono.omg.domain.Product;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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

    // home
    @GetMapping("/omg")
    public String home() {
        return "main/index";
    }

    // 검색 페이지 (기존 상품 페이지)
    @GetMapping("/omg/search")
    public String mainPage(@RequestParam("q") String query, @PageableDefault(size = 10) Pageable pageable, Model model) {
        model.addAttribute("query", query);
        model.addAttribute("nowPage", pageable.getPageNumber());
        return "main/mainPage";
    }

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

    @GetMapping("/admin/management")
    public String adminProductManagement(@PageableDefault(size = 10) Pageable pageable, Model model) {
        model.addAttribute("nowPage", pageable.getPageNumber());
        return "admin/managedProducts";
    }

    //상세페이지
    @GetMapping("/products/detail/{productId}")
    public String detailProductPage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "detail/detailProduct";
    }

    // 마이 페이지
    @GetMapping("/accounts/mypage")
    public String accountMyPage(Model model) {
        List<Product> products = productRepository.findAll();
        List<RegistedProductResponseDto> responseDto = new ArrayList<>();

        for (Product product : products) {
            responseDto.add(new RegistedProductResponseDto(product));
        }

        model.addAttribute("products", responseDto);

        return "mypage/accountPrivatePage";
    }

//    //리뷰 조회
//    @GetMapping("/api/products/detail/{productId}")
//    public String getReviewList(Model model){
//        List<Review> reviews = reviewRepository.findAll();
//        List<ReviewResponseDto> responseDto = new ArrayList<>();
//
//        for (Review review : reviews) {
//            String username = accountRepository.findUsernameByAccountId(review.getId());
//
//            responseDto.add(new ReviewResponseDto(review.getProduct().getId(), username, review.getReviewContent(), review.getCreatedAt() ,review.getModifiedAt()));
//        }
//        model.addAttribute("reviews", responseDto);
//
//        return "product/detailProduct";
//    }
}

