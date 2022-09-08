package com.knud4.an.report.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Role;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.exception.NotAuthenticatedException;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.report.dto.CreateReportForm;
import com.knud4.an.report.dto.ReportDTO;
import com.knud4.an.report.entity.Report;
import com.knud4.an.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Long createReport(CreateReportForm form, Account writer) {
        Report report = Report.builder()
                .writer(writer)
                .title(form.getTitle())
                .content(form.getContent())
                .build();
        reportRepository.save(report);
        return report.getId();
    }

    public ReportDTO findReportById(Long reportId, Long accountId) throws NotFoundException{
        Report findReport = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("민원을 찾을 수 없습니다."));
        Account account = accountRepository.findAccountById(accountId)
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));
        if(account.getRole() != Role.ROLE_MANAGER &&
                !findReport.getWriter().getId().equals(accountId))
            throw new IllegalStateException("접근 권한이 없습니다.");
        return new ReportDTO(findReport);
    }

    public List<Report> findAll() {
        List<Report> reports = reportRepository.findAll();
        return reports;
    }

    public List<Report> findAll(int page, int cnt) {
        validatePaging(page, cnt);
        List<Report> reports = reportRepository.findAll(page, cnt);
        return reports;
    }

    public List<Report> findByAccountId(int page, int cnt, Long accountId) {
        validatePaging(page, cnt);
        List<Report> reports = reportRepository.findByAccountId(page, cnt, accountId);
        return reports;
    }

    public Boolean isLastPage(int page, int cnt) {
        validatePaging(page, cnt);
        Long reportCnt = reportRepository.countAll();
        return (long) page*cnt >= reportCnt;
    }

    public Boolean isFirstPage(int page) {
        return page == 1;
    }

    private void validatePaging(int page, int cnt) {
        Long num = reportRepository.countAll();
        int limit = (page - 1) * cnt;
        if (page != 1 && num<=limit) {
            throw new IllegalStateException("게시글 요청 범위를 초과하였습니다.");
        }
    }
}
