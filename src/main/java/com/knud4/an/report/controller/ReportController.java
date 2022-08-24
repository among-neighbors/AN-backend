package com.knud4.an.report.controller;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.report.dto.CreateReportForm;
import com.knud4.an.report.dto.ReportDTO;
import com.knud4.an.report.service.ReportService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final AccountService accountService;

    @Operation(summary = "민원 생성")
    @PostMapping("/api/v1/reports/new")
    public ApiSuccessResult<Long> createReport(@Valid @RequestBody CreateReportForm form,
                                               HttpServletRequest req) {
        String accountEmail = (String) req.getAttribute("email");
        Account account = accountService.findAccountByEmail(accountEmail);
        Long reportId = reportService.createReport(form, account);
        return ApiUtil.success(reportId);
    }

    @Operation(summary = "민원 전체 조회")
    @GetMapping("/api/v1/manager/reports")
    public ApiSuccessResult<List<ReportDTO>> findAll(@RequestParam(name = "page") int page,
                                                     @RequestParam(name = "count") int count) {
        return ApiUtil.success(reportService.findAll(page, count));
    }

    @Operation(summary = "민원 전체 조회 (계정)")
    @GetMapping("/api/v1/reports")
    public ApiSuccessResult<List<ReportDTO>> findByAccountId(@RequestParam(name = "page") int page,
                                                             @RequestParam(name = "count") int count,
                                                             HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(reportService.findByAccountId(page, count, accountId));
    }

    @Operation(summary = "민원 상세 조회 (id)")
    @GetMapping("/api/v1/reports/{id}")
    public ApiSuccessResult<ReportDTO> findById(@PathVariable(name = "id") Long id,
                                                HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(reportService.findReportById(id, accountId));
    }

}
