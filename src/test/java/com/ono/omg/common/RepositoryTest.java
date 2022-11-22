package com.ono.omg.common;

import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.cart.CartRepository;
import com.ono.omg.repository.like.LikeRepository;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.review.ReviewRepository;
import com.ono.omg.repository.token.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
abstract class RepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

}
