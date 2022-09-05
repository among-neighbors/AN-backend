package com.knud4.an.community.entity;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.board.Board;
import com.knud4.an.board.Scope;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends Board {

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "likes")
    private Long like;

    private String writerLineName;

    private String writerHouseName;


    @Builder
    public Community(String title, String content, Profile writer,
                     Category category, Long like, Scope scope) {
        this.setContent(content);
        this.setTitle(title);
        this.setWriter(writer);
        this.setScope(scope);

        this.category = category;
        this.like = like;
        Account account = writer.getAccount();
        this.writerLineName = account.getLine().getName();
        this.writerHouseName = account.getHouse().getName();
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void increaseLike() {
        this.like++;
    }
}
