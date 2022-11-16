package com.ono.omg.controller.view;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.ProductResponseDto;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.*;

@Controller
public class UIController {
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

    // 재고 관리 페이지
    @GetMapping("/admin/management")
    public String managedPage(Model model) {
        List<Product> products = productRepository.findAll();
        List<AllProductManagementResponseDto> responseDto = new ArrayList<>();

        for (Product product : products) {
            responseDto.add(new AllProductManagementResponseDto(product));
        }
        model.addAttribute("products", responseDto);

        return "admin/managedProducts";
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

