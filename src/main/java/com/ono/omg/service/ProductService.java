package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.common.ProductReqDto;
import com.ono.omg.repository.OrdersRepository;
import com.ono.omg.repository.ProductRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public String createProduct(ProductReqDto productReqDto) {
        Product product = new Product(productReqDto);
        productRepository.save(product);
        return "상품등록 완료";
    }
}
