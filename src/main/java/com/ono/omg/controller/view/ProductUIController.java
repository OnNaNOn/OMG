package com.ono.omg.controller.view;

import com.ono.omg.dto.response.ProductResponseDto;
import com.ono.omg.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.ono.omg.dto.response.ProductResponseDto.*;

@Controller
@Slf4j
public class ProductUIController {

    private ProductRepository productRepository;

    public ProductUIController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 관리자 재고 관리 페이지
     */
    @GetMapping("/admin/management")
    public String managedPage(Pageable pageable, Model model) {
        Page<AllProductManagementResponseDto> productStock = productRepository.findAllProductStock(pageable);
        model.addAttribute("products", productStock);
        model.addAttribute("maxPage", productStock.getTotalPages());
        model.addAttribute("productsSize", Long.valueOf(productStock.getTotalElements()));

        return "admin/managedProducts";
    }
}
