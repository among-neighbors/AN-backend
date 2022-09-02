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
        if(writer.getAccount().getRole() != Role.ROLE_MANAGER) {
            throw new NotAuthenticatedException("작성 권한이 없습니다.");
        }
        Report report = reportRepository.findById(form.getBoardId());
        if(report == null) {
            throw new NotFoundException("민원글을 찾을 수 없습니다.");
        }
        ReportComment comment = ReportComment.builder()
                .writer(writer)
                .text(form.getText())
                .report(report)
                .build();
        commentRepository.save(comment);
        return comment.getId();
    }

    public List<ReportComment> findAllByReportId(int page, int count, Long reportId) {
        return commentRepository.findAllByReportId(reportId, page, count);
    }

    @Transactional
    public void deleteById(Long id) {
        commentRepository.delete(
                commentRepository.findById(id).orElseThrow(() -> new NotFoundException("댓글이 존재하지 않습니다."))
        );
    }

    public Boolean isFirstPage(int page) {
        return page == 1;
    }

    public Boolean isLastPage(int page, int count, Long reportId) {
        Long commentCnt = commentRepository.findCommentCountByReportId(reportId);
        return (long) (page + 2) * count >= commentCnt;
    }

}
