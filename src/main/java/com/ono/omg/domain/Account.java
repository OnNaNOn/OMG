package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.ono.omg.dto.request.AccountRequestDto.AccountRegisterRequestDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    // enum 으로 변경
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private DeletedType deletedType;

    public Account(AccountRegisterRequestDto accountRegisterRequestDto) {
        this.accountType = AccountType.ROLE_STANDARD;
        this.username = accountRegisterRequestDto.getUsername();
        this.password = accountRegisterRequestDto.getPassword();
        this.deletedType = DeletedType.DELETE_NO;
    }

    public void upgradeAdmin() {
        this.accountType = AccountType.ROLE_ADMIN;
    }

    public void deleteAccount() {
        deletedType = DeletedType.DELETE_YES;
    }
}
