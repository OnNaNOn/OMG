package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Order;
import com.ono.omg.domain.Product;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ono.omg.dto.response.OrderResponseDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 주문하기
     */
    @Transactional
    public CreatedOrdersResponseDto productOrder(Long productId, Account account) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT)
        );

        Account findAccount = accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );
        // 상품에 대한 주문은 여러개도 발생할 수 있다..?
        Order savedOrder = new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice()));

        return new CreatedOrdersResponseDto(
                savedOrder.getId(),
                savedOrder.getTotalPrice(),
                savedOrder.getAccount().getUsername(),
                savedOrder.getProduct()
        );
    }


    @Transactional
    public void testDecrease(Long productId, Account account) {
        Product findProduct = productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT)
        );

        Account findAccount = accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );

        /**
         * 아래 문장을 Order 생성자에서 실행되도록 변경
         */
//        findProduct.decrease();

        // 상품에 대한 주문은 여러개도 발생할 수 있다..?
        Order savedOrder = new Order(findAccount, findProduct, getTotalOrderPrice(findProduct.getPrice()));
//        productRepository.saveAndFlush(findProduct);
//        orderRepository.saveAndFlush(savedOrder);
    }

    /**
     * 주문에 상품이 여러개일 경우를 대비해 별도의 메서드로 분리
     */
    private Integer getTotalOrderPrice(int price) {
        return price;
    }

    public List<CreatedOrdersResponseDto> findAllOrders(Account account) {
        List<CreatedOrdersResponseDto> findOrders = orderRepository.findOrdersParticularAccount(account.getId());

        return findOrders;
    }
}
