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
public abstract class Board extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile writer;

    private Range range;

    protected void setTitle(String title) {
        this.title = title;
    }

    protected void setContent(String content) {
        this.content = content;
    }

    protected void setWriter(Profile writer) {
        this.writer = writer;
    }

    protected void setRange(Range range) {
        this.range = range;
    }
}
