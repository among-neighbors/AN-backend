package com.knud4.an.comment.entity;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.account.entity.Profile;
import lombok.Getter;

import javax.persistence.*;


@Getter
@MappedSuperclass
public abstract class Comment extends BaseEntity {

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile writer;

    protected void setText(String text) {
        this.text = text;
    }

    protected void setWriter(Profile writer) {
        this.writer = writer;
    }
}
