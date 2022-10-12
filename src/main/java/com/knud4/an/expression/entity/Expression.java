package com.knud4.an.expression.entity;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.community.entity.Community;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expression {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Community board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @Builder
    public Expression(Community board, Profile profile) {
        this.board = board;
        this.profile = profile;
    }
}
