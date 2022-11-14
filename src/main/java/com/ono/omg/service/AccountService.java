package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.RefreshToken;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.RefreshTokenRepository;
import com.ono.omg.security.jwt.JwtUtil;
import com.ono.omg.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.ono.omg.dto.AccountRequestDto.*;
import static com.ono.omg.dto.AccountResponseDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Transactional
    public AccountRegisterResponseDto signup(AccountRegisterRequestDto accountRegisterRequestDto) {
        accountRepository.findByUsername(accountRegisterRequestDto.getUsername()).ifPresent(Account -> {
            throw new CustomCommonException(ErrorCode.DUPLICATE_USERNAME);
        });

        if(!accountRegisterRequestDto.getPassword().equals(accountRegisterRequestDto.getPasswordConfirm())){
            throw new CustomCommonException(ErrorCode.NOT_EQUAL_PASSWORD);
        }

        // Password Encode
        accountRegisterRequestDto.passwordEncode(passwordEncoder.encode(accountRegisterRequestDto.getPassword()));

        Account account = new Account(accountRegisterRequestDto);
        accountRepository.save(account);

        return new AccountRegisterResponseDto(account);
    }

    public AccountLoginResponseDto login(AccountLoginRequestDto accountLoginRequestDto, HttpServletResponse response) {
        Account findAccount = accountRepository.findByUsername(accountLoginRequestDto.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.DUPLICATE_USERNAME)
        );

        if(!passwordEncoder.matches(accountLoginRequestDto.getPassword(), findAccount.getPassword())){
            throw new CustomCommonException(ErrorCode.NOT_EQUAL_PASSWORD);
        }

        TokenDto tokenDto = jwtUtil.createAllToken(accountLoginRequestDto.getUsername());
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUsername(findAccount.getUsername());

        if(findRefreshToken.isPresent()) {
            refreshTokenRepository.save(findRefreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else { // Refresh Token이 없는 경우
            RefreshToken newRefreshToken = new RefreshToken(tokenDto.getRefreshToken(), findAccount.getUsername());
            refreshTokenRepository.save(newRefreshToken);
        }
        addTokenHeader(response, tokenDto);

        return new AccountLoginResponseDto(findAccount);
    }

    private void addTokenHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }

}
