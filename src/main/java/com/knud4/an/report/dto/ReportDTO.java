package com.knud4.an.report.dto;

import com.knud4.an.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long reportId;
    private String title;
    private String content;

    private String lineName;
    private String houseName;

    private LocalDateTime createdDate;

    public ReportDTO(Report report) {
        this.reportId = report.getId();
        this.title = report.getTitle();
        this.content = report.getContent();

        this.houseName = report.getHouseName();
        this.lineName = report.getLineName();

        this.createdDate = report.getCreatedDate();
    }
}
