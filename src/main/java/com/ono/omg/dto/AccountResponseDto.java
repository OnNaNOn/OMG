package com.ono.omg.dto;

import com.ono.omg.domain.Account;
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
        private String grade;

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public AccountRegisterResponseDto(Account account) {
            this.username = account.getUsername();
            this.grade = account.getGrade();
            this.createdAt = account.getCreatedAt();
            this.modifiedAt = account.getModifiedAt();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountLoginResponseDto {
        private String username;
        private String grade;

        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public AccountLoginResponseDto(Account account) {
            this.username = account.getUsername();
            this.grade = account.getGrade();
            this.createdAt = account.getCreatedAt();
            this.modifiedAt = account.getModifiedAt();
        }
    }
}
