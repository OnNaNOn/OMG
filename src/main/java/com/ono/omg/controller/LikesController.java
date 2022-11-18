package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class LikesController {

    private final LikeService likeService;

    /**
     * 좋아요 기능
     */
    @PostMapping("/{productId}/like")
    public ResponseEntity<ResponseDto<String>> addLikes(@PathVariable Long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(ResponseDto.success(likeService.addLikes(productId, userDetails.getAccount())), HttpStatus.OK);
    }
}
