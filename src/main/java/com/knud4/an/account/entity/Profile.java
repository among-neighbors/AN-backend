package com.knud4.an.account.entity;

import com.knud4.an.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String name;
    private Integer age;
    private String pin;

//    @OneToMany(mappedBy = "writer")
//    private List<CommunityComment> communityComments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "writer")
//    private List<Community> communities = new ArrayList<>();

    @Builder
    public Profile(Account account, Gender gender, String name, Integer age, String pin) {
        this.account = account;
        this.gender = gender;
        this.name = name;
        this.age = age;
        this.pin = pin;
    }
}
