package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Cart;
import com.ono.omg.repository.cart.CartRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;

    /**
     * 상품 장바구니 담기
     */
    public Long inputProduct(Long productId, UserDetailsImpl userDetails) {
        Account account = userDetails.getAccount();

        Optional<Cart> found = cartRepository.findByProductIdAndAccountId(productId, account.getId());

        if(found.isEmpty()){
            Cart cart = new Cart(account, productId);
            cartRepository.save(cart);
            return cart.getId();
        } else{
            throw new IllegalArgumentException("해당 상품은 장바구니에 담겨져 있습니다.");
        }

    }
}
