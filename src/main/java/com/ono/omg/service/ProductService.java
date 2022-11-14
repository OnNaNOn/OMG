package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.common.ProductReqDto;
import com.ono.omg.repository.AccountRepository;
import com.ono.omg.repository.OrdersRepository;
import com.ono.omg.repository.ProductRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final AccountRepository accountRepository;

    // 상품등록
    public String createProduct(ProductReqDto productReqDto, Account account) {

        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인하지 않은 사용자입니다"));

        Product product = new Product(productReqDto);
        productRepository.save(product);
        return "상품등록 완료";
    }

    // 상품수정
    public String updateProduct(Long productId, ProductReqDto productReqDto, Account account) {

        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인하지 않은 사용자입니다"));

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 ID를 찾을 수 없습니다"));

        product.updateProduct(productReqDto);

        return "상품수정 완료";
    }


    public String deleteProduct(Long productId, Account account) {
        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인하지 않은 사용자입니다"));

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 ID를 찾을 수 없습니다"));

    }
}
