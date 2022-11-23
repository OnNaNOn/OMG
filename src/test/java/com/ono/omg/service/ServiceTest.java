package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.request.ProductReqDto;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.token.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.ono.omg.dto.request.AccountRequestDto.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
abstract class ServiceTest {

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected CartService cartService;

    @Autowired
    protected LikeService likeService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected ReviewService reviewService;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected AccountRegisterRequestDto registerAccountInfo(String username) {
        return new AccountRegisterRequestDto(username, "pw", "pw");
    }

    protected AccountLoginRequestDto loginAccountInfo(String username) {
        return new AccountLoginRequestDto(username, "pw");
    }

    protected AccountLoginRequestDto loginAdminAccountInfo(String username) {
        return new AccountLoginRequestDto(username, "pw", "ohmygod");
    }

    protected Account createAccount(String username) {
        return new Account(new AccountRegisterRequestDto(username, "pw", "pw"));
    }

    protected Product createProduct(String title, Integer stock) {
        return new Product(title, 1000, "카테고리", "배송상태", stock, (long) stock);
    }

    protected Product createProductExistAccount(String title, Integer stock, Long sellerId) {
        return new Product(title, 1000, "카테고리", "배송상태", stock, sellerId);
    }

    protected ProductReqDto createProductInfoWithLogin(String title, Integer stock) {
        return new ProductReqDto(title, 1000, stock,"카테고리",  "배송상태", "https://jaesa-bucket.s3.ap-northeast-2.amazonaws.com/omg.jpg");
    }

}
