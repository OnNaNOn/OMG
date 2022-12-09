package com.ono.omg.dto.response;

import com.ono.omg.domain.Account;
import com.ono.omg.domain.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AccountResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountRegisterResponseDto {
        private String username;
        private AccountType accountType;

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public AccountRegisterResponseDto(Account account) {
            this.username = account.getUsername();
            this.accountType = account.getAccountType();
            this.createdAt = account.getCreatedAt();
            this.modifiedAt = account.getModifiedAt();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountLoginResponseDto {
        private String username;
        private AccountType accountType;

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public AccountLoginResponseDto(Account account) {
            this.username = account.getUsername();
            this.accountType = account.getAccountType();
            this.createdAt = account.getCreatedAt();
            this.modifiedAt = account.getModifiedAt();
        }
    }
}
