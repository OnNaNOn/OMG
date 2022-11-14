package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Like;
import com.ono.omg.domain.Product;
import com.ono.omg.repository.AccountRepository;
import com.ono.omg.repository.LikeRepository;
import com.ono.omg.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final ProductRepository productRepository;

    private final AccountRepository accountRepository;

    //상품 좋아요
    @Transactional
    public String addLikes(long productId, Account account) {
        accountRepository.findById(account.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인하지 않은 사용자입니다"));

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("상품 ID를 찾을 수 없습니다"));

        Like like = new Like(product, account.getId());
        likeRepository.save(like);

        return "좋아요 완료";
    }
}
