package com.knud4.an.comment.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue
    private Long id;

    private String text;

    // Member와의 연관관계 설정 필요

    // Community와의 연관관계 설정 필요

    private Long like;
}
