package com.knud4.an.community.controller;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.board.Range;
import com.knud4.an.community.dto.CommunityDTO;
import com.knud4.an.community.dto.CreateCommunityForm;
import com.knud4.an.community.entity.Category;
import com.knud4.an.community.service.CommunityService;
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
public class CommunityController {

    private final CommunityService communityService;
    private final AccountService accountService;

    @Operation(summary = "커뮤니티글 생성")
    @PostMapping("/api/v1/communities/new")
    public ApiUtil.ApiSuccessResult<Long> createCommunity(@Valid @RequestBody CreateCommunityForm form,
                                                          HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        Account account = accountService.findAccountByAccountId(accountId);
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long communityId = communityService.createCommunity(form, account, profile);
        return ApiUtil.success(communityId);
    }

    @Operation(summary = "커뮤니티글 전체 조회")
    @GetMapping("/api/v1/communities")
    public ApiUtil.ApiSuccessResult<List<CommunityDTO>> findAll(@RequestParam(name = "page") int page,
                                                                @RequestParam(name = "count") int count,
                                                                @RequestParam(name = "range") Range range,
                                                                @RequestParam(name = "category") Category category,
                                                                HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        if(category.equals(Category.ALL)) {
            if (range.equals(Range.LINE))
                return ApiUtil.success(CommunityDTO.entityListToDTOList(communityService.findAllMyLine(page, count, accountId)));
            return ApiUtil.success(CommunityDTO.entityListToDTOList(communityService.findAll(page, count, accountId)));
        }
        if (range.equals(Range.LINE))
            return ApiUtil.success(CommunityDTO.entityListToDTOList(communityService.findMyLineByCategory(category, page, count, accountId)));
        return ApiUtil.success(CommunityDTO.entityListToDTOList(communityService.findByCategory(category, page, count, accountId)));

    }

    @Operation(summary = "커뮤니티글 상세 조회 (id)")
    @GetMapping("/api/v1/communities/{id}")
    public ApiSuccessResult<CommunityDTO> findById(@PathVariable(name = "id") Long id,
                                                   HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(new CommunityDTO(communityService.findCommunityById(id, accountId)));
    }
}
