package com.knud4.an.board;

import com.knud4.an.base.BaseEntity;
import com.knud4.an.account.entity.Profile;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile writer;

    @Enumerated(EnumType.STRING)
    private Scope scope;

    protected void setTitle(String title) {
        this.title = title;
    }

    protected void setContent(String content) {
        this.content = content;
    }

    protected void setWriter(Profile writer) {this.writer = writer;}

    protected void setScope(Scope scope) {
        this.scope = scope;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeScope(Scope scope) {
        this.scope = scope;
    }
}
