package com.ono.omg.domain;

import com.ono.omg.dto.request.AccountRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("좋아요의")
class LikeTest {

    @Nested
    @DisplayName("Create 메서드는")
    class Create {

        @Test
        @DisplayName("한 번 누르면 좋아요를 등록한다.")
        public void 좋아요_누른다() throws Exception {
            // given
            Account account = new Account(new AccountRequestDto.AccountRegisterRequestDto("jae", "pw", "pw"));
            Like like = new Like(1L, account);

            // when & then
            assertThat(like.getProductId()).isEqualTo(1L);
            assertThat(like.getAccount().getUsername()).isEqualTo(account.getUsername());
        }
    }
}