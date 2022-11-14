package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.repository.order.OrderRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    public void productOrder(Long productId, Account account) {
//        productRepository.findById(productId).orElseThrow(
//                () -> new CustomCommonException(ErrorCode.)
//        )
    }
}
