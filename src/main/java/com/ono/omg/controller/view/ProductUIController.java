package com.ono.omg.controller.view;

import com.ono.omg.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.ono.omg.dto.response.ProductResponseDto.AllProductManagementResponseDto;

@Controller
@Slf4j
public class ProductUIController {
    private ProductRepository productRepository;

    public ProductUIController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    
}
