package com.knud4.an.comment.controller;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.comment.dto.CommentDTO;
import com.knud4.an.comment.dto.CommentListDTO;
import com.knud4.an.comment.dto.CreateCommentForm;
import com.knud4.an.comment.service.CommunityCommentService;
import com.knud4.an.comment.service.ReportCommentService;
import com.knud4.an.annotation.AccountRequired;
import com.knud4.an.annotation.ProfileRequired;
import com.knud4.an.utils.api.ApiUtil;
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

@Tag(name = "댓글")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommunityCommentService communityCommentService;
    private final ReportCommentService reportCommentService;
    private final AccountService accountService;

    @ProfileRequired
    @Operation(summary = "커뮤니티 댓글 생성", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티 댓글 생성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "프로필 또는 게시글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/comments/communities")
    public ApiUtil.ApiSuccessResult<Long> createCommunityComment(@Valid @RequestBody CreateCommentForm form,
                                                                 HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long commentId = communityCommentService.createCommunityComment(form, profile);
        return ApiUtil.success(commentId);
    }

    @AccountRequired
    @Operation(summary = "커뮤니티 댓글 조회 (community_id)", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티 댓글 조회 성공", content = @Content(schema = @Schema(implementation = CommentListDTO.class))),
            @ApiResponse(responseCode = "400", description = "댓글 요청 범위를 초과하였습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/comments/communities/{id}")
    public ApiUtil.ApiSuccessResult<CommentListDTO> findAllByCommunityId(@PathVariable(name = "id") Long id,
                                                                         @RequestParam(name = "page") int page,
                                                                         @RequestParam(name = "count") int count,
                                                                         HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(new CommentListDTO(communityCommentService.isFirstPage(page),
                communityCommentService.isLastPage(page, count, id),
                CommentDTO.makeCommunityCommentList(communityCommentService.findAllByCommunityId(page, count, id),
                        accountId)));
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티 댓글 삭제", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티 댓글 삭제 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "댓글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class)))
    })
    @DeleteMapping("/api/v1/comments/communities/{id}")
    public ApiUtil.ApiSuccessResult<String> deleteCommunityComment(@PathVariable(name = "id") Long id) {
        communityCommentService.deleteById(id);
        return ApiUtil.success("삭제되었습니다.");
    }

    @ProfileRequired
    @Operation(summary = "민원 댓글 생성", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "민원 댓글 생성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "프로필 또는 게시글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/manager/comments/reports")
    public ApiUtil.ApiSuccessResult<Long> createReportComment(@Valid @RequestBody CreateCommentForm form,
                                                              HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long commentId = reportCommentService.createReportComment(form, profile);
        return ApiUtil.success(commentId);
    }

    @AccountRequired
    @Operation(summary = "민원 댓글 조회 (report_id)", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "민원 댓글 조회 성공", content = @Content(schema = @Schema(implementation = CommentListDTO.class))),
            @ApiResponse(responseCode = "400", description = "댓글 요청 범위를 초과하였습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/comments/reports/{id}")
    public ApiUtil.ApiSuccessResult<CommentListDTO> findAllByReportId(@PathVariable Long id,
                                                                      @RequestParam int page,
                                                                      @RequestParam int count,
                                                                      HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(new CommentListDTO(reportCommentService.isFirstPage(page),
                reportCommentService.isLastPage(page, count, id),
                CommentDTO.makeReportCommentList(reportCommentService.findAllByReportId(page, count, id),
                        accountId)));
    }

    @ProfileRequired
    @Operation(summary = "민원 댓글 삭제", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "민원 댓글 삭제 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiUtil.ApiErrorResult.class)))
    })
    @DeleteMapping("/api/v1/comments/reports/{id}")
    public ApiUtil.ApiSuccessResult<String> deleteReportComment(@PathVariable(name = "id") Long id) {
        reportCommentService.deleteById(id);
        return ApiUtil.success("삭제되었습니다.");
    }
}
