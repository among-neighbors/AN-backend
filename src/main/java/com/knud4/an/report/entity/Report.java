package com.knud4.an.report.entity;

import com.knud4.an.Base.BaseEntity;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.comment.entity.ReportComment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile writer;

    @OneToMany(mappedBy = "report", cascade = CascadeType.REMOVE)
    private List<ReportComment> comments = new ArrayList<>();

    @Builder
    public Report(String title, String content, Profile writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }
}
