package com.knud4.an.account.entity;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.house.entity.House;
import com.knud4.an.line.entity.Line;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    private String email;
    private String username;
    private String password;

    private String lineName;
    private String houseName;

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.ROLE_NOT_SIGNED;

    @Builder
    public Account(House house, Line line, String email, String username, String password, Role role) {
        this.house = house;
        this.line = line;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;

        this.lineName = line.getName();
        this.houseName = house.getName();
    }
}
