package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Like;
import com.ono.omg.domain.Product;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.like.LikeRepository;
import com.ono.omg.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
                () -> new CustomCommonException(ErrorCode.FORBIDDEN_USER));

        productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));

        Optional<Like> likes = likeRepository.findByProductIdAndAccountId(productId, account);

        if (likes.isEmpty()) {
            Like like = new Like(productId, account);
            likeRepository.save(like);
            return "좋아요 완료";
        } else {
            likeRepository.deleteByProductId(likes.get().getProductId());
            return "좋아요가 취소되었습니다";
        }
    }
}
