package com.ono.omg.domain;

import com.ono.omg.domain.base.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Account extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private Long id;

    // enum 으로 변경
    private String grade;

    private String username;
    private String password;
}
