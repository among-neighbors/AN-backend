package com.knud4.an.report.entity;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.account.entity.Account;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    private String houseName;

    private String lineName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account writer;

    @Builder
    public Report(String title, String content, Account writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.houseName = writer.getHouse().getName();
        this.lineName = writer.getLine().getName();
    }
}
