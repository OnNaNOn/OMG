package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Cart;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.cart.CartRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final AccountRepository accountRepository;

    /**
     * 상품 장바구니 담기
     */
    @Transactional
    public Long inputProduct(Long productId, UserDetailsImpl userDetails) {
        Account account = accountRepository.findByUsername(userDetails.getAccount().getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );

        Optional<Cart> found = cartRepository.findByProductIdAndAccountId(productId, account.getId());

        log.info("found -----------> " + found.toString());

        if (found.isEmpty()) {
            Cart cart = new Cart(account, productId);
            cartRepository.save(cart);
        } else {
            throw new CustomCommonException(ErrorCode.DUPLICATE_PRODUCT);
        }
        return account.getId();
    }
}

