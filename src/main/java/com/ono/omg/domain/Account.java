package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private DeletedType deletedType = DeletedType.DELETE_NO;

    public Account(AccountRegisterRequestDto accountRegisterRequestDto) {
        this.accountType = AccountType.ROLE_STANDARD;
        this.username = accountRegisterRequestDto.getUsername();
        this.password = accountRegisterRequestDto.getPassword();
    }

    public Account(Long id, AccountType accountType, String username, String password, DeletedType deletedType) {
        this.id = id;
        this.accountType = accountType;
        this.username = username;
        this.password = password;
        this.deletedType = deletedType;
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
        deletedType = DeletedType.DELETE_YES;
    }
}
