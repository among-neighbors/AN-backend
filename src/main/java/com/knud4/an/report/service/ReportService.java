package com.knud4.an.report.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.report.dto.CreateReportForm;
import com.knud4.an.report.dto.ReportDTO;
import com.knud4.an.report.entity.Report;
import com.knud4.an.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reportRepository;

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
        Report findReport = reportRepository.findById(reportId);
        if (findReport == null || !findReport.getId().equals(accountId)) {
            throw new NotFoundException("민원을 찾을 수 없습니다.");
        }
        return new ReportDTO(findReport);
    }

    public List<ReportDTO> findAll() {
        List<Report> reports = reportRepository.findAll();
        List<ReportDTO> reportDTOS = new ArrayList<>();

        for (Report r : reports) {
            reportDTOS.add(new ReportDTO(r));
        }

        return reportDTOS;
    }

    public List<ReportDTO> findAll(int page, int cnt) {
        List<Report> reports = reportRepository.findAll(page, cnt);
        List<ReportDTO> reportDTOS = new ArrayList<>();

        for (Report r : reports) {
            reportDTOS.add(new ReportDTO(r));
        }

        return reportDTOS;
    }

    public List<ReportDTO> findByAccountId(int page, int cnt, Long accountId) {
        List<Report> reports = reportRepository.findByAccountId(page, cnt, accountId);
        List<ReportDTO> reportDTOS = new ArrayList<>();

        for (Report r : reports) {
            reportDTOS.add(new ReportDTO(r));
        }

        return reportDTOS;
    }


}
