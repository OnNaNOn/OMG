package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.response.OrderResponseDto;
import com.ono.omg.dto.response.OrderResponseDto.MainPageOrdersResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 좋아요 조회(limit 10 order by modifiedAt desc)
     * */
    @GetMapping("/likes")
    ResponseEntity<ResponseDto<List<MainPageOrdersResponseDto>>> likesDetails(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(ResponseDto.success(likeService.likesDetails(userDetails)), HttpStatus.OK);
    }
}
