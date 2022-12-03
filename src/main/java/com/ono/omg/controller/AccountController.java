package com.ono.omg.controller;

import com.ono.omg.dto.common.ResponseDto;
import com.ono.omg.security.user.UserDetailsImpl;
import com.ono.omg.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.ono.omg.dto.request.AccountRequestDto.AccountLoginRequestDto;
import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
import static com.ono.omg.dto.response.AccountResponseDto.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/accounts")
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
    public ResponseDto<AccountLoginResponseDto> login(@RequestBody @Valid AccountLoginRequestDto accountLoginRequestDto,
                                                      HttpServletResponse response) {
        return ResponseDto.success(accountService.login(accountLoginRequestDto, response));
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/unregister")
    public ResponseDto<UnregisterUser> eraseAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseDto.success(accountService.unregister(userDetails.getAccount()));
    }
}
