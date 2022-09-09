package com.knud4.an.report.controller;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.annotation.AccountRequired;
import com.knud4.an.report.dto.CreateReportForm;
import com.knud4.an.report.dto.ReportDTO;
import com.knud4.an.report.dto.ReportListDTO;
import com.knud4.an.report.entity.Report;
import com.knud4.an.report.service.ReportService;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "민원")
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final AccountService accountService;

    @AccountRequired
    @Operation(summary = "민원 생성", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "민원 생성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "계정이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/reports/new")
    public ApiSuccessResult<Long> createReport(@Valid @RequestBody CreateReportForm form,
                                               HttpServletRequest req) {
        String accountEmail = (String) req.getAttribute("email");
        Account account = accountService.findAccountByEmail(accountEmail);
        Long reportId = reportService.createReport(form, account);
        return ApiUtil.success(reportId);
    }

    @Operation(summary = "민원 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "민원 전체 조회 성공", content = @Content(schema = @Schema(implementation = ReportListDTO.class))),
            @ApiResponse(responseCode = "400", description = "민원글 요청 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/manager/reports")
    public ApiSuccessResult<?> findAll(@RequestParam(name = "page") int page,
                                                     @RequestParam(name = "count") int count) {
        List<Report> reports = reportService.findAll(page, count);
        ReportListDTO dto = new ReportListDTO(
                reportService.isFirstPage(page),
                reportService.isLastPage(page, count, reportService.countAll()),
                reports);
        return ApiUtil.success(dto);
    }

    @AccountRequired
    @Operation(summary = "민원 전체 조회 (계정)", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "민원 전체 조회 성공", content = @Content(schema = @Schema(implementation = ReportListDTO.class))),
            @ApiResponse(responseCode = "400", description = "민원글 요청 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/reports")
    public ApiSuccessResult<?> findByAccountId(@RequestParam(name = "page") int page,
                                                             @RequestParam(name = "count") int count,
                                                             HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        List<Report> reports = reportService.findByAccountId(page, count, accountId);
        ReportListDTO dto = new ReportListDTO(
                reportService.isLastPage(page, count, reportService.countByAccountId(accountId)),
                reportService.isFirstPage(page),
                reports);
        return ApiUtil.success(dto);
    }

    @AccountRequired
    @Operation(summary = "민원 상세 조회 (id)", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "민원 상세 조회 성공", content = @Content(schema = @Schema(implementation = ReportDTO.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "민원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/reports/{id}")
    public ApiSuccessResult<ReportDTO> findById(@PathVariable(name = "id") Long id,
                                                HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(reportService.findReportById(id, accountId));
    }

}
