package com.knud4.an.comment.entity;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.community.entity.Community;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String text;

    // Member와의 연관관계 설정 필요

    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

    private Long likes;
}
