package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.dto.response.ProductResponseDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;
import static com.ono.omg.dto.response.ProductResponseDto.ProductResDto;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    private final AccountRepository accountRepository;


    // 상품등록
    @Transactional
    public String createProduct(ProductReqDto productReqDto, Account account) {
        validAccount(account);

        Product product = new Product(productReqDto, account);
        productRepository.save(product);
        return "상품 등록 완료";
    }

    //상품조회
    public ProductResDto searchProduct(Long productId) {
        Product product = validProduct(productId);

        return new ProductResDto(product);
    }

    //상품조회
    public ProductResponseDto.DetailProductResponseDto mainPage(Long productId) {
        return new ProductResponseDto.DetailProductResponseDto(productRepository.detailProduct(productId));
    }


    private Account validAccount(Account account) {
        return accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND));
    }

    private Product validProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));
    }
}
