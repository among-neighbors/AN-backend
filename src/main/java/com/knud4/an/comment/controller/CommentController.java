package com.knud4.an.comment.controller;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.comment.dto.CommentDTO;
import com.knud4.an.comment.dto.CreateCommentForm;
import com.knud4.an.comment.service.CommunityCommentService;
import com.knud4.an.comment.service.ReportCommentService;
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

    @Operation(summary = "커뮤니티 댓글 생성")
    @PostMapping("/api/v1/profile/comment/community/new")
    public ApiUtil.ApiSuccessResult<Long> createCommunityComment(@Valid @RequestBody CreateCommentForm form,
                                                                 HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long commentId = communityCommentService.createCommunityComment(form, profile);
        return ApiUtil.success(commentId);
    }

    @Operation(summary = "커뮤니티 댓글 조회 (community_id)")
    @GetMapping("/api/v1/profile/comment/community/{id}")
    public ApiUtil.ApiSuccessResult<List<CommentDTO>> findAllByCommunityId(@PathVariable(name = "id") Long id,
                                                                              @RequestParam(name = "page") int page,
                                                                              @RequestParam(name = "count") int count) {
        return ApiUtil.success(CommentDTO.makeCommunityCommentList(communityCommentService.findAllByCommunityId(page, count, id)));
    }

    @Operation(summary = "민원 댓글 생성")
    @PostMapping("/api/v1/profile/comment/report/new")
    public ApiUtil.ApiSuccessResult<Long> createReportComment(@Valid @RequestBody CreateCommentForm form,
                                                              HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long commentId = reportCommentService.createReportComment(form, profile);
        return ApiUtil.success(commentId);
    }

    @Operation(summary = "민원 댓글 조회 (report_id)")
    @GetMapping("/api/v1/profile/comment/report/{id}")
    public ApiUtil.ApiSuccessResult<List<CommentDTO>> findAllByReportId(@PathVariable Long id,
                                                                        @RequestParam int page,
                                                                        @RequestParam int count) {
        return ApiUtil.success(CommentDTO.makeReportCommentList(reportCommentService.findAllByReportId(page, count, id)));
    }
}
