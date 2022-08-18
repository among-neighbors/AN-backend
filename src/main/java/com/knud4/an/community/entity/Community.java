package com.knud4.an.community.entity;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.board.Board;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends Board {

    private Category category;

    private Long likes;

    private Range range;

    private String writerLine;

    @Builder
    public Community(String title, String content, Profile writer,
                     Category category, Long likes, Range range, Account account) {
        this.setContent(content);
        this.setTitle(title);
        this.setWriter(writer);

        this.category = category;
        this.likes = likes;
        this.range = range;
        this.writerLine = account.getLine().getName();
    }

}
