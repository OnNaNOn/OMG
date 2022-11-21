package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.dto.response.OrderResponseDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ono.omg.dto.response.ProductResponseDto.MainPageResponseDto;
import static com.ono.omg.dto.response.ProductResponseDto.ProductResDto;
import static java.util.stream.Collectors.toList;

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

        validAccount(account);

        Product product = new Product(productReqDto, account);
        productRepository.save(product);
        return "상품등록 완료";
    }

    // 상품수정
    @Transactional
    public String updateProduct(Long productId, ProductReqDto productReqDto, Account account) {

        validAccount(account);
        validSeller(account);
        Product product = validProduct(productId);

        product.updateProduct(productReqDto);
            return "상품수정 완료";
        }


    //상품삭제
    @Transactional
    public String deleteProduct(Long productId, Account account) {
        validAccount(account);

        validSeller(account);

        Product product = validProduct(productId);

        product.isDeleted();

        return "상품삭제 완료";
    }

    //상품조회
    @Transactional(readOnly = true)
    public ProductResDto searchProduct(Long productId) {
        Product product = validProduct(productId);


        return new ProductResDto(product);
    }

    /**
     * 상품 등록내역 조회
     * */
    @Transactional(readOnly = true)
    public List<MainPageOrdersResponseDto> registerDetailsProduct(Pageable pageable, UserDetailsImpl userDetails) {
        Long accountId = userDetails.getAccount().getId();

        List<Product> registerProductList = productRepository.findRegisterProductList(pageable, accountId);

        return registerProductList.stream()
                .map((o)-> new MainPageOrdersResponseDto(o.getId(), o.getImgUrl(), o.getTitle()))
                .collect(toList());
    }


    private Account validAccount(Account account) {
        return accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND));
    }

    private Product validSeller(Account account) {
        return productRepository.findBySellerId(account.getId()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_SELLER));
    }

    private Product validProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));
    }


}
