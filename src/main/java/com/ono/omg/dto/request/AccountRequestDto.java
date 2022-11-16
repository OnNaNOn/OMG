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

        @NotEmpty
        private String username;

        @NotEmpty
        private String password;

        @NotEmpty
        private String passwordConfirm;

        public void passwordEncode(String encodedPassword) {
            this.password = encodedPassword;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountLoginRequestDto {

        @NotEmpty
        private String username;

        @NotEmpty
        private String password;
    }

}
