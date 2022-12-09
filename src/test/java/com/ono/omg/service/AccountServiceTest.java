package com.ono.omg.service;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.AccountType;
import com.ono.omg.exception.CustomCommonException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import static com.ono.omg.dto.request.AccountRequestDto.AccountLoginRequestDto;
import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
import static com.ono.omg.dto.response.AccountResponseDto.AccountLoginResponseDto;
import static com.ono.omg.dto.response.AccountResponseDto.AccountRegisterResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DisplayName("AccountServiceTest 의")
class AccountServiceTest extends ServiceTest {

    @Test
    @DisplayName("signup 메서드는 회원가입을 한다.")
    public void 사용자_회원_가입() throws Exception {
        // given & when
        AccountRegisterResponseDto account = accountService.signup(new AccountRegisterRequestDto("jae", "pw", "pw"));

        // then
        assertThat(account.getUsername()).isEqualTo("jae");
    }

    @Test
    @DisplayName("signup 메서드는 중복된 이름이 존재할 경우 예외를 발생시킨다.")
    public void 사용자_회원가입_중복이름_예외() throws Exception {
        // given
        accountRepository.save(createAccount("jae"));

        // when
        CustomCommonException exception = assertThrows(CustomCommonException.class, () -> {
            accountService.signup(registerAccountInfo("jae"));
        });

        // then
        assertEquals("중복된 닉네임이 존재합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("signup 메서드는 중복된 이름이 존재할 경우 예외를 발생시킨다.")
    public void 사용자_회원가입_비밀번호_불일치_예외() throws Exception {
        // given & when
        CustomCommonException exception = assertThrows(CustomCommonException.class, () -> {
            accountService.signup(new AccountRegisterRequestDto("jae", "pw", "pw2"));
        });

        // then
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("login 메서드는 정상적인 사용자인 경우에 토큰을 발생한다.")
    public void 사용자_로그인() throws Exception {
        // given
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        accountService.signup(registerAccountInfo("jae"));

        // when
        AccountLoginResponseDto account = accountService.login(loginAccountInfo("jae"), mockResponse);

        // then
        assertThat(account.getUsername()).isEqualTo("jae");
    }

    @Test
    @DisplayName("login 메서드에 관리자 비밀 키를 넣어 로그인할 경우 관리자 권한을 갖는다.")
    public void 관리자_로그인() throws Exception {
        // given
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        accountService.signup(registerAccountInfo("jae"));

        // when
        AccountLoginResponseDto account = accountService.login(loginAdminAccountInfo("jae"), mockResponse);

        // then
        assertThat(account.getUsername()).isEqualTo("jae");
        assertThat(account.getAccountType()).isEqualTo(AccountType.ROLE_ADMIN);
    }

    @Test
    @DisplayName("login 메서드는 저장된 비밀번호와 일치하지 않는 경우 예외가 발생한다.")
    public void 로그인시_비밀번호_불일치() throws Exception {
        // given
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
        accountService.signup(registerAccountInfo("jae")).getUsername();

        // when
        CustomCommonException exception = assertThrows(CustomCommonException.class, () -> {
            accountService.login(new AccountLoginRequestDto("jae", "incorrect password"), mockResponse);
        });

        // then
        assertThat("비밀번호가 일치하지 않습니다.").isEqualTo(exception.getMessage());
    }
}