package com.knud4.an.comment.entity;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.report.entity.Report;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportComment extends Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @Builder
    public ReportComment(Report report, String text, Profile writer) {
        this.report = report;
        this.setText(text);
        this.setWriter(writer);
    }
}
