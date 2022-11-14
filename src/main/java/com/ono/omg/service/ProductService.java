package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.common.ProductReqDto;
import com.ono.omg.dto.common.ProductResDto;
import com.ono.omg.repository.AccountRepository;
import com.ono.omg.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final AccountRepository accountRepository;

    // 상품등록
    @Transactional
    public String createProduct(ProductReqDto productReqDto, Account account) {

        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인하지 않은 사용자입니다"));

        Product product = new Product(productReqDto, account);
        productRepository.save(product);
        return "상품등록 완료";
    }

    // 상품수정
    @Transactional
    public String updateProduct(Long productId, ProductReqDto productReqDto, Account account) {

        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인하지 않은 사용자입니다"));

        productRepository.findByUserid(account.getId()).orElseThrow(
                () -> new IllegalArgumentException("수정 권한이 없는 사용자입니다"));

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 ID를 찾을 수 없습니다"));

        product.updateProduct(productReqDto, account);
            return "상품수정 완료";
        }
        
        
    //상품삭제
    @Transactional
    public String deleteProduct(Long productId, Account account) {
        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인하지 않은 사용자입니다"));

        productRepository.findByUserid(account.getId()).orElseThrow(
                () -> new IllegalArgumentException("삭제 권한이 없는 사용자입니다"));

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 ID를 찾을 수 없습니다"));

        productRepository.delete(product);
            return "상품삭제 완료";
    }

    //상품조회
    @Transactional(readOnly = true)
    public ProductResDto searchProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 ID를 찾을 수 없습니다"));

        return new ProductResDto(product);
    }
}
