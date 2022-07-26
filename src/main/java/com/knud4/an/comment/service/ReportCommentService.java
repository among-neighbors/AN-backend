package com.knud4.an.comment.service;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.entity.Role;
import com.knud4.an.comment.dto.CreateCommentForm;
import com.knud4.an.comment.entity.ReportComment;
import com.knud4.an.comment.repository.ReportCommentRepository;
import com.knud4.an.exception.NotAuthenticatedException;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.report.entity.Report;
import com.knud4.an.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportCommentService {

    private final ReportCommentRepository commentRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public Long createReportComment(CreateCommentForm form, Profile writer) {
        Report report = reportRepository.findById(form.getBoardId())
                .orElseThrow(() -> new NotFoundException("민원글을 찾을 수 없습니다."));
        ReportComment comment = ReportComment.builder()
                .writer(writer)
                .text(form.getText())
                .report(report)
                .build();
        commentRepository.save(comment);
        return comment.getId();
    }

    public List<ReportComment> findAllByReportId(int page, int count, Long reportId) {
        validatePaging(page, count, reportId);
        reportRepository.findById(reportId).orElseThrow(() -> new NotFoundException("민원글이 존재하지 않습니다."));
        return commentRepository.findAllByReportId(reportId, page, count);
    }

    @Transactional
    public void deleteById(Long id) {
        commentRepository.delete(
                commentRepository.findById(id).orElseThrow(() -> new NotFoundException("댓글이 존재하지 않습니다."))
        );
    }

    public Boolean isLastPage(int page, int cnt, Long reportId) {
        validatePaging(page, cnt, reportId);
        Long commentCnt = commentRepository.findCommentCountByReportId(reportId);
        return (long) page*cnt >= commentCnt;
    }

    public Boolean isFirstPage(int page) {
        return page == 1;
    }

    private void validatePaging(int page, int cnt, Long reportId) {
        Long num = commentRepository.findCommentCountByReportId(reportId);
        int limit = (page - 1) * cnt;
        if (page != 1 && num<=limit) {
            throw new IllegalStateException("댓글 요청 범위를 초과하였습니다.");
        }
    }

}
