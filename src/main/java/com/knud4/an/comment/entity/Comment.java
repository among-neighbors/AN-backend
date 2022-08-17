package com.knud4.an.comment.entity;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.account.entity.Profile;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

    private Long likes;
}
