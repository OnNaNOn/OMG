package com.ono.omg.controller;

import com.ono.omg.domain.Product;
import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikesController {

    private final LikeService likeService;

    @PostMapping("/{productId}/like")
    public ResponseEntity<ResponseDto<String>> createLikes(@PathVariable long productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(ResponseDto.success(likeService.createLikes(productId, userDetails)), HttpStatus.CREATED);

}
