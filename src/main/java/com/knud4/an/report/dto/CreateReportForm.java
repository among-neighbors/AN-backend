package com.knud4.an.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportForm {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
