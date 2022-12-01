package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.Like;
import com.ono.omg.domain.Product;
import com.ono.omg.dto.response.OrderResponseDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.like.LikeRepository;
import com.ono.omg.repository.product.ProductRepository;
import com.ono.omg.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class LikeService {


    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;


    //상품 좋아요
    @Transactional
    public String addLikes(Long productId, Long accountId) {

        Account findAccount = accountRepository.findById(accountId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND));

        productRepository.findById(productId).orElseThrow(
                () -> new CustomCommonException(ErrorCode.NOT_FOUND_PRODUCT));

        Optional<Like> likes = likeRepository.findByProductIdAndAccount(productId, findAccount);

        if (likes.isEmpty()) {
            likeRepository.save(new Like(productId, findAccount));
            return "좋아요 완료";
        } else {
            likeRepository.deleteByProductId(likes.get().getProductId());
            return "좋아요가 취소되었습니다";
        }
    }

    /**
     * 좋아요 조회
     * */
    @Transactional(readOnly = true)
    public List<MainPageOrdersResponseDto> likesDetails(UserDetailsImpl userDetails) {
        Long accountId = userDetails.getAccount().getId();
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "modifiedAt");
        List<Like> detailsList = likeRepository.findDetailsList(pageable, accountId);

        List<MainPageOrdersResponseDto> dtoList = new ArrayList<>();
        for (Like like : detailsList) {
            Long productId = like.getProductId();
            Product product = productRepository.findById(productId).orElseThrow();

            dtoList.add(new MainPageOrdersResponseDto(product.getId(), product.getImgUrl(), product.getTitle()));
        }
        return dtoList;
    }
}
