package com.knud4.an.community.entity;

import com.knud4.an.board.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends Board {

    private Category category;

    // Comment와의 연관관계 추가 필요

    private Long commentCnt;

    private Long like;
}
