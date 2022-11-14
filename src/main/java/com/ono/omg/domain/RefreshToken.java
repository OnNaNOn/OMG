package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String username;

    public RefreshToken(String token, String username) {
        this.refreshToken = token;
        this.username = username;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }

}
