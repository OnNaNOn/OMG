package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.dto.response.ProductResDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final AccountRepository accountRepository;

    public List<MainPageResponseDto> findAllSavedProducts() {
        List<Product> products = productRepository.findAll();
        List<MainPageResponseDto> responseDto = new ArrayList<>();

        for (Product product : products) {
            responseDto.add(new MainPageResponseDto(product));
        }
        return responseDto;
    }

    // 상품등록
    @Transactional
    public String createProduct(ProductReqDto productReqDto, Account account) {

        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND));

        Product product = new Product(productReqDto, account);
        productRepository.save(product);
        return "상품등록 완료";
    }

    // 상품수정
    @Transactional
    public String updateProduct(Long productId, ProductReqDto productReqDto, Account account) {

        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND));

        productRepository.findBySellerId(account.getId()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_SELLER));

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));

        product.updateProduct(productReqDto);
            return "상품수정 완료";
        }


    //상품삭제
    @Transactional
    public String deleteProduct(Long productId, Account account) {
        accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND));

        productRepository.findBySellerId(account.getId()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_SELLER));

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));

        product.isDeleted();

        return "상품삭제 완료";
    }

    //상품조회
    @Transactional(readOnly = true)
    public ProductResDto searchProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));

        return new ProductResDto(product);
    }
}
