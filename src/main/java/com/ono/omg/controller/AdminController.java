package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.dto.response.AdminResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    /**
     * 관리자 재고 관리 페이지
     */
    @GetMapping("/management")
    public ResponseDto<AdminResponseDto> adminPage(@RequestParam(name = "page") Integer page,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(adminService.ManagedProduct(page, userDetails.getAccount()));
    }
}
