package com.knud4.an.board;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.account.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class Board extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    private Range range;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account writer;

    protected void setTitle(String title) {
        this.title = title;
    }

    protected void setContent(String content) {
        this.content = content;
    }

    protected void setWriter(Account writer) {
        this.writer = writer;
    }

    public void setRange(Range range) {
        this.range = range;
    }
}
