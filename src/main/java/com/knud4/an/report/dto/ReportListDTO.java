package com.knud4.an.report.dto;

import com.knud4.an.report.entity.Report;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReportListDTO {
    private Boolean isLastPage;
    private Boolean isFirstPage;
    private List<ReportDTO> list = new ArrayList<>();

    public ReportListDTO(Boolean isLastPage, Boolean isFirstPage, List<Report> reportList) {
        this.isLastPage = isLastPage;
        this.isFirstPage = isFirstPage;

        reportList.forEach(r -> {
            list.add(new ReportDTO(r));
        });
    }
}
