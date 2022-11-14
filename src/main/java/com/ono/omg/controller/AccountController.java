package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.ono.omg.dto.request.AccountRequestDto.AccountLoginRequestDto;
import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
import static com.ono.omg.dto.response.AccountResponseDto.AccountLoginResponseDto;
import static com.ono.omg.dto.response.AccountResponseDto.AccountRegisterResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseDto<AccountRegisterResponseDto> registerAccount(@RequestBody @Valid AccountRegisterRequestDto accountRequestDto) {
        return ResponseDto.success(accountService.signup(accountRequestDto));
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseDto<AccountLoginResponseDto> login(@RequestBody AccountLoginRequestDto accountLoginRequestDto, HttpServletResponse response) {
        return ResponseDto.success(accountService.login(accountLoginRequestDto, response));
    }
}
