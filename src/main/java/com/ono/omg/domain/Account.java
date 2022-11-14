package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.ono.omg.dto.AccountRequestDto.AccountRegisterRequestDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    // enum 으로 변경
    private String grade;

    private String username;
    private String password;

    public Account(AccountRegisterRequestDto accountRegisterRequestDto) {
        this.grade = "ROLE_USER";
        this.username = accountRegisterRequestDto.getUsername();
        this.password = accountRegisterRequestDto.getPassword();
    }
}
