package com.ono.omg.controller.view;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.ProductResponseDto;
import com.ono.omg.repository.product.ProductRepository;
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

}

