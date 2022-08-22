package com.knud4.an.community.entity;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.board.Board;
import com.knud4.an.board.Range;
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

    private String writerLineName;

    private String writerHouseName;

    private String writerName;

    @Builder
    public Community(String title, String content, Account writer,
                     Category category, Long likes, Range range, Profile profile) {
        this.setContent(content);
        this.setTitle(title);
        this.setWriter(writer);
        this.setRange(range);

        this.category = category;
        this.likes = likes;
        this.writerLineName = writer.getLine().getName();
        this.writerHouseName = writer.getHouse().getName();
        this.writerName = profile.getName();
    }

}
