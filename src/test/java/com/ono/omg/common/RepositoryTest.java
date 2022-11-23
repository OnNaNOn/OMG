//package com.ono.omg.common;
//
//import com.ono.omg.domain.Account;
//import com.ono.omg.domain.Order;
//import com.ono.omg.domain.Product;
//import com.ono.omg.dto.response.ProductResponseDto;
//import com.ono.omg.repository.account.AccountRepository;
//import com.ono.omg.repository.cart.CartRepository;
//import com.ono.omg.repository.like.LikeRepository;
//import com.ono.omg.repository.order.OrderRepository;
//import com.ono.omg.repository.product.ProductRepository;
//import com.ono.omg.repository.review.ReviewRepository;
//import com.ono.omg.repository.token.RefreshTokenRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.ActiveProfiles;
//
//import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
//
//@SpringBootTest
//@ActiveProfiles("test")
//public abstract class RepositoryTest {
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Autowired
//    CartRepository cartRepository;
//
//    @Autowired
//    LikeRepository likeRepository;
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    ProductRepository productRepository;
//
//    @Autowired
//    ReviewRepository reviewRepository;
//
//    @Autowired
//    RefreshTokenRepository refreshTokenRepository;
//
//
//    protected Account saveAccount(String username) {
//        Account account = new Account(new AccountRegisterRequestDto(username, "pw", "pw"));
//        return accountRepository.save(account);
//    }
//
//    protected Order saveOrders() {
//        for (int i = 0; i < 5; i++) {
//            Product givenProduct = productRepository.save(new Product("항해" + i, 1000 + i, "스파르타" + i, "빠른 배송", 10 + i, (long) i));
//            Account givenAccount = accountRepository.save(new Account(new AccountRegisterRequestDto("선원" + i, "pw", "pw")));
//            orderRepository.save(new Order(givenAccount, givenProduct, givenProduct.getPrice()));
//        }
//        Account givenAccount = accountRepository.save(new Account(new AccountRegisterRequestDto("선원", "pw", "pw")));
//        Product givenProduct = productRepository.save(new Product("항해", 1000, "스파르타", "빠른 배송", 10, (long) 6L));
//        return orderRepository.save(new Order(givenAccount, givenProduct, givenProduct.getPrice()));
//    }
//
//    protected Page<ProductResponseDto.AllProductManagementResponseDto> findAllProductHasStock() {
//        int max = 5;
//        for (int i = 0; i < max; i++) {
//            productRepository.save(new Product("항해" + i, 1000 + i, "스파르타" + i, "빠른 배송", i, (long) i));
//        }
//        return productRepository.findAllProductStock(PageRequest.ofSize(1));
//    }
//
//}
