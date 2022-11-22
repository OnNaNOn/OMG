//package com.ono.omg.domain;
//
//import com.ono.omg.dto.request.AccountRequestDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@DisplayName("사용자의")
//public class AccountTest {
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @Nested
//    @DisplayName("Create 메서드는 ")
//    class Create {
//        @DisplayName("생성할 경우 권한이 ROLE_STANDARD이다.")
//        @Test
//        public void 사용자_생성() throws Exception {
//            // given
//            Account account = new Account(new AccountRequestDto.AccountRegisterRequestDto("jae", "pw", "pw"));
//
//            // when & then
//            assertThat(account.getUsername()).isSameAs("jae");
//            assertThat(account.getPassword()).isSameAs("pw");
//            assertThat(account.getAccountType()).isSameAs(AccountType.ROLE_STANDARD);
//        }
//    }
//
//    @Nested
//    @DisplayName("Update 메서드는 ")
//    class Update {
//
//        @DisplayName("사용자의 권한을 관리자(ROLE_ADMIN)로 변경한다.")
//        @Test
//        public void 사용자_관리자로_변경() throws Exception {
//            // given
//            Account account = new Account(new AccountRegisterRequestDto("jae", passwordEncoder.encode("pw"), passwordEncoder.encode("pw")));
//
//            // when
//            account.upgradeAdmin();
//
//            // then
//            assertThat(account.getAccountType()).isEqualTo(AccountType.ROLE_ADMIN);
//        }
//    }
//
//    @DisplayName("Delete 메서드는")
//    @Nested
//    class Delete {
//
//        @Test
//        @DisplayName("회원을 탈퇴한다. 단, YN 필드를 Y로 변경시킨다.")
//        public void 회원_탈퇴() throws Exception {
//            // given
//            Account account = new Account(new AccountRegisterRequestDto("jae", passwordEncoder.encode("pw"), passwordEncoder.encode("pw")));
//
//            // when
//            account.deleteAccount();
//
//            // then
//            assertThat(account.getIsDeleted()).isEqualTo("Y");
//        }
//
//    }
//}
//
