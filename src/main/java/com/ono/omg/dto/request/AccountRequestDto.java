package com.ono.omg.dto.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;

public class AccountRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class AccountRegisterRequestDto {

        @NotEmpty(message = "username을 입력해주세요.")
        private String username;

        @NotEmpty(message = "password를 입력해주세요.")
        private String password;

        @NotEmpty(message = "password를 입력해주세요.")
        private String passwordConfirm;

        public void passwordEncode(String encodedPassword) {
            this.password = encodedPassword;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class AccountLoginRequestDto {

        @NotEmpty(message = "username을 입력해주세요.")
        private String username;

        @NotEmpty(message = "password를 입력해주세요.")
        private String password;

        String adminSecretKey = "";
    }

}
