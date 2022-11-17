package com.ono.omg.controller.view;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;
import com.ono.omg.dto.response.ProductResponseDto.detailProductResponseDto;
import com.ono.omg.repository.product.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.RegistedProductResponseDto;

@Controller
public class UIController {
    // 계층형 아키텍처에 맞게 재고관리와 메인 페이지는
    // Controller > Service > Repository로 변경할 필요 있음
    private ProductRepository productRepository;

    public UIController(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

    //메인페이지
    @GetMapping("/api/products")
    public String mainPage(Model model){
        List<Product>products = productRepository.findAll();
        List<MainPageResponseDto> responseDto = new ArrayList<>();

        for(Product product : products){
            responseDto.add(new MainPageResponseDto(product));
        }
        model.addAttribute("products", responseDto);

        return "main/mainPage";
    }

    //상세페이지
    @GetMapping("/api/products/detail/{productId}")
    public String detailProductPage(Model model, @PathVariable Long productId){
        Product product = productRepository.detailProduct(productId);
        detailProductResponseDto responseDto = new detailProductResponseDto(product);
        model.addAttribute("product", responseDto);

        return "product/detailProduct";
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

}

