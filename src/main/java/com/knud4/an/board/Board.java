package com.knud4.an.board;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.account.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@MappedSuperclass
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile writer;

    private Range range;

    @Builder
    public Board(String title, String content, Profile writer, Range range) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.range = range;
    }

}
