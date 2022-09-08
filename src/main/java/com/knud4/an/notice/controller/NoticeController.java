package com.knud4.an.notice.controller;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.board.Scope;
import com.knud4.an.annotation.AccountRequired;
import com.knud4.an.annotation.ProfileRequired;
import com.knud4.an.notice.dto.CreateNoticeForm;
import com.knud4.an.notice.dto.NoticeDTO;
import com.knud4.an.notice.dto.NoticeListDTO;
import com.knud4.an.notice.entity.Notice;
import com.knud4.an.notice.service.NoticeService;
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

@Tag(name = "공지사항")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    private final AccountService accountService;

    @ProfileRequired
    @Operation(summary = "공지사항 생성", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 생성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "프로필이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/manager/notices")
    public ApiSuccessResult<Long> createNotice(@Valid @RequestBody CreateNoticeForm form,
                                               HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long noticeId = noticeService.createNotice(form, profile);
        return ApiUtil.success(noticeId);
    }

    @AccountRequired
    @Operation(summary = "공지사항 전체 조회", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 전체 조회 성공", content = @Content(schema = @Schema(implementation = NoticeListDTO.class))),
            @ApiResponse(responseCode = "400", description = "게시글 요청 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/notices")
    public ApiSuccessResult<NoticeListDTO> findAll(@RequestParam(name = "page") int page,
                                                   @RequestParam(name = "count") int count,
                                                   @RequestParam(name = "scope") Scope scope,
                                                   HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        List<Notice> noticeList;
        if(scope.equals(Scope.LINE))
            noticeList = noticeService.findAllMyLine(page, count, accountId);
        else noticeList = noticeService.findAll(page, count, accountId);
        return ApiUtil.success(new NoticeListDTO(noticeService.isFirstPage(page),
                noticeService.isLastPage(page, count),
                NoticeDTO.entityListToDTOList(noticeList)));
    }

    @AccountRequired
    @Operation(summary = "공지사항 상세 조회 (id)", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 상세 조회 성공", content = @Content(schema = @Schema(implementation = NoticeDTO.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "공지글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/notices/{id}")
    public ApiSuccessResult<NoticeDTO> findById(@PathVariable(name = "id") Long id,
                                                HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        Notice notice = noticeService.findNoticeById(id, accountId);
        NoticeDTO noticeDTO = new NoticeDTO(notice);
        noticeDTO.setIsMine(noticeService.isMine(notice, accountId));
        return ApiUtil.success(new NoticeDTO());
    }

    @ProfileRequired
    @Operation(summary = "공지사항 수정", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 수정 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "수정 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "공지글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PutMapping("/api/v1/manager/notices/{id}")
    public ApiSuccessResult<Long> updateNotice(@PathVariable(name = "id") Long id,
                                               @Valid @RequestBody NoticeDTO noticeDTO,
                                               HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        noticeService.updateNotice(id, noticeDTO, profileId);
        return ApiUtil.success(id);
    }

    @AccountRequired
    @Operation(summary = "공지사항 삭제", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 삭제 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "공지글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @DeleteMapping("/api/v1/manager/notices/{id}")
    public ApiSuccessResult<String> deleteById(@PathVariable(name = "id") Long id) {
        noticeService.deleteById(id);
        return ApiUtil.success("삭제되었습니다.");
    }
}
