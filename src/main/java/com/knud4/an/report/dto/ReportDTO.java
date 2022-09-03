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
    private Long id;
    private String title;
    private String content;

    private Writer writer;

    private LocalDateTime createdDate;

    public ReportDTO(Report report) {
        this.id = report.getId();
        this.title = report.getTitle();
        this.content = report.getContent();

        writer = new Writer(report.getLineName(), report.getHouseName(), report.getWriter().getId());

        this.createdDate = report.getCreatedDate();
    }

    @Data
    @AllArgsConstructor
    private static class Writer {
        private String lineName;
        private String houseName;
        private Long id;
    }

}
