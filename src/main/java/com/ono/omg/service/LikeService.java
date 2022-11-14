package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Like;
import com.ono.omg.repository.LikeRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public String createLikes(long productId, Account account) {
        Like like = likeRepository.findByproduct(productId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다"));

        Like like =


        likeRepository.save()

    }
}
