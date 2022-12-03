package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.RefreshToken;
import com.ono.omg.exception.CustomCommonException;
import com.ono.omg.exception.ErrorCode;
import com.ono.omg.repository.account.AccountRepository;
import com.ono.omg.repository.token.RefreshTokenRepository;
import com.ono.omg.security.jwt.JwtUtil;
import com.ono.omg.security.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.ono.omg.dto.request.AccountRequestDto.AccountLoginRequestDto;
import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
import static com.ono.omg.dto.response.AccountResponseDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value(value = "${admin.secret.key}")
    private String ADMIN_SECRET_KEY;

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

    @Transactional
    public AccountLoginResponseDto login(AccountLoginRequestDto accountLoginRequestDto, HttpServletResponse response) {
        Account findAccount = accountRepository.findByUsername(accountLoginRequestDto.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.DUPLICATE_USERNAME)
        );

        // 탈퇴한 회원
        isUnregisterUser(findAccount);

        // 비밀번호 일치
        isSameLoginPasswordAndSavePassword(accountLoginRequestDto, findAccount);

        // 관리자 번호와 일치할 시 관리자 등급으로 변경
        validateAdminSecretKey(accountLoginRequestDto, findAccount);

        TokenDto tokenDto = jwtUtil.createAllToken(accountLoginRequestDto.getUsername());

        createAndSaveToken(findAccount, tokenDto);
        addTokenHeader(response, tokenDto);

        return new AccountLoginResponseDto(findAccount);
    }

    @Transactional
    public UnregisterUser unregister(Account account) {
        Account findAccount = accountRepository.findByUsername(account.getUsername()).orElseThrow(
                () -> new CustomCommonException(ErrorCode.USER_NOT_FOUND)
        );
        findAccount.deleteAccount();

        return new UnregisterUser(findAccount.getUsername());
    }

    private void createAndSaveToken(Account findAccount, TokenDto tokenDto) {
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUsername(findAccount.getUsername());

        if(findRefreshToken.isPresent()) {
            refreshTokenRepository.save(findRefreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else { // Refresh Token이 없는 경우
            refreshTokenRepository.save(new RefreshToken(tokenDto.getRefreshToken(), findAccount.getUsername()));
        }
    }

    private void validateAdminSecretKey(AccountLoginRequestDto accountLoginRequestDto, Account findAccount) {
        String adminSecretKey = accountLoginRequestDto.getAdminSecretKey();
        log.info("adminSecretKey = " + adminSecretKey);

        if (hasAdminAuthorized(adminSecretKey)) {
            findAccount.upgradeAdmin();
            accountRepository.save(findAccount);
        }
    }

    private void isSameLoginPasswordAndSavePassword(AccountLoginRequestDto accountLoginRequestDto, Account findAccount) {
        if(!passwordEncoder.matches(accountLoginRequestDto.getPassword(), findAccount.getPassword())){
            throw new CustomCommonException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }

    private void isUnregisterUser(Account findAccount) {
        if(findAccount.getIsDeleted().equals("Y")) {
            throw new CustomCommonException(ErrorCode.UNREGISTER_USER);
        }
    }

    private boolean hasAdminAuthorized(String adminSecretKey) {
        return hasAdminSecretKey(adminSecretKey) & adminSecretKey.equals(ADMIN_SECRET_KEY);
    }

    private boolean hasAdminSecretKey(String adminSecretKey) {
        return StringUtils.hasText(adminSecretKey);
    }

    private void addTokenHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getRefreshToken());
    }
}
