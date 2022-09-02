package com.knud4.an.comment.controller;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.comment.dto.CommentDTO;
import com.knud4.an.comment.dto.CommentListDTO;
import com.knud4.an.comment.dto.CreateCommentForm;
import com.knud4.an.comment.service.CommunityCommentService;
import com.knud4.an.comment.service.ReportCommentService;
import com.knud4.an.interceptor.AccountRequired;
import com.knud4.an.interceptor.ProfileRequired;
import com.knud4.an.utils.api.ApiUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommunityCommentService communityCommentService;
    private final ReportCommentService reportCommentService;
    private final AccountService accountService;

    @ProfileRequired
    @Operation(summary = "커뮤니티 댓글 생성")
    @PostMapping("/api/v1/comments/communities/new")
    public ApiUtil.ApiSuccessResult<Long> createCommunityComment(@Valid @RequestBody CreateCommentForm form,
                                                                 HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long commentId = communityCommentService.createCommunityComment(form, profile);
        return ApiUtil.success(commentId);
    }

    @AccountRequired
    @Operation(summary = "커뮤니티 댓글 조회 (community_id)")
    @GetMapping("/api/v1/comments/communities/{id}")
    public ApiUtil.ApiSuccessResult<CommentListDTO> findAllByCommunityId(@PathVariable(name = "id") Long id,
                                                                         @RequestParam(name = "page") int page,
                                                                         @RequestParam(name = "count") int count) {
        return ApiUtil.success(new CommentListDTO(communityCommentService.isFirstPage(page),
                communityCommentService.isLastPage(page, count, id),
                CommentDTO.makeCommunityCommentList(communityCommentService.findAllByCommunityId(page, count, id))));
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티 댓글 삭제")
    @DeleteMapping("/api/v1/comments/communities/{id}")
    public ApiUtil.ApiSuccessResult<String> deleteCommunityComment(@PathVariable(name = "id") Long id) {
        communityCommentService.deleteById(id);
        return ApiUtil.success("삭제되었습니다.");
    }

    @ProfileRequired
    @Operation(summary = "민원 댓글 생성")
    @PostMapping("/api/v1/comments/reports/new")
    public ApiUtil.ApiSuccessResult<Long> createReportComment(@Valid @RequestBody CreateCommentForm form,
                                                              HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long commentId = reportCommentService.createReportComment(form, profile);
        return ApiUtil.success(commentId);
    }

    @AccountRequired
    @Operation(summary = "민원 댓글 조회 (report_id)")
    @GetMapping("/api/v1/comments/reports/{id}")
    public ApiUtil.ApiSuccessResult<CommentListDTO> findAllByReportId(@PathVariable Long id,
                                                                        @RequestParam int page,
                                                                        @RequestParam int count) {
        return ApiUtil.success(new CommentListDTO(reportCommentService.isFirstPage(page),
                reportCommentService.isLastPage(page, count, id),
                CommentDTO.makeReportCommentList(reportCommentService.findAllByReportId(page, count, id))));
    }

    @ProfileRequired
    @Operation(summary = "민원 댓글 삭제")
    @DeleteMapping("/api/v1/comments/reports/{id}")
    public ApiUtil.ApiSuccessResult<String> deleteReportComment(@PathVariable(name = "id") Long id) {
        reportCommentService.deleteById(id);
        return ApiUtil.success("삭제되었습니다.");
    }
}
