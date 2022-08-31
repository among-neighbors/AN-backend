package com.knud4.an.notice.controller;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.board.Range;
import com.knud4.an.notice.dto.CreateNoticeForm;
import com.knud4.an.notice.dto.NoticeDTO;
import com.knud4.an.notice.service.NoticeService;
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
public class NoticeController {

    private final NoticeService noticeService;

    private final AccountService accountService;

    @Operation(summary = "공지사항 생성")
    @PostMapping("/api/v1/manager/notices/new")
    public ApiSuccessResult<Long> createNotice(@Valid @RequestBody CreateNoticeForm form,
                                                       HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long noticeId = noticeService.createNotice(form, profile);
        return ApiUtil.success(noticeId);
    }

    @Operation(summary = "공지사항 전체 조회")
    @GetMapping("/api/v1/notices")
    public ApiSuccessResult<List<NoticeDTO>> findAll(@RequestParam(name = "page") int page,
                                                     @RequestParam(name = "count") int count,
                                                     @RequestParam(name = "range") Range range,
                                                     HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        if(range.equals(Range.LINE))
            return ApiUtil.success(NoticeDTO.entityListToDTOList(noticeService.findAllMyLine(page, count, accountId)));
        return ApiUtil.success(NoticeDTO.entityListToDTOList(noticeService.findAll(page, count, accountId)));
    }

    @Operation(summary = "공지사항 상세 조회 (id)")
    @GetMapping("/api/v1/notices/{id}")
    public ApiSuccessResult<NoticeDTO> findById(@PathVariable(name = "id") Long id,
                                                HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(new NoticeDTO(noticeService.findNoticeById(id, accountId)));
    }

    @Operation(summary = "공지사항 삭제")
    @DeleteMapping("/api/v1/notices/{id}/delete")
    public ApiSuccessResult<String> deleteById(@PathVariable(name = "id") Long id,
                                               HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        noticeService.deleteById(id, accountId);
        return ApiUtil.success("삭제되었습니다.");
    }
}
