package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;

@Entity
@Getter
@NoArgsConstructor
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String isDeleted = "N";

    private String username;
    private String password;

    public Account(AccountRegisterRequestDto accountRegisterRequestDto) {
        this.accountType = AccountType.ROLE_STANDARD;
        this.username = accountRegisterRequestDto.getUsername();
        this.password = accountRegisterRequestDto.getPassword();
    }

    /**
     * 관리자 번호로 로그인 시 admin으로 등급업
     */
    public void upgradeAdmin() {
        this.accountType = AccountType.ROLE_ADMIN;
    }

    /**
     * 회원 탈퇴
     */
    public void deleteAccount() {
        isDeleted = "Y";
    }
}
