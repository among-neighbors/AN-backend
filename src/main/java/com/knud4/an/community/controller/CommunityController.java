package com.knud4.an.community.controller;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.board.Range;
import com.knud4.an.community.dto.CommunityDTO;
import com.knud4.an.community.dto.CommunityListDTO;
import com.knud4.an.community.dto.CreateCommunityForm;
import com.knud4.an.community.entity.Category;
import com.knud4.an.community.service.CommunityService;
import com.knud4.an.annotation.AccountRequired;
import com.knud4.an.annotation.ProfileRequired;
import com.knud4.an.utils.api.ApiUtil;
import com.knud4.an.utils.api.ApiUtil.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "커뮤니티")
@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    private final AccountService accountService;

    @ProfileRequired
    @Operation(summary = "커뮤니티글 생성")
    @PostMapping("/api/v1/communities")
    public ApiUtil.ApiSuccessResult<Long> createCommunity(@Valid @RequestBody CreateCommunityForm form,
                                                          HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long communityId = communityService.createCommunity(form, profile);
        return ApiUtil.success(communityId);
    }

    @AccountRequired
    @Operation(summary = "커뮤니티글 전체 조회")
    @GetMapping("/api/v1/communities")
    public ApiUtil.ApiSuccessResult<CommunityListDTO> findAll(@RequestParam(name = "page") int page,
                                                              @RequestParam(name = "count") int count,
                                                              @RequestParam(name = "range") Range range,
                                                              @RequestParam(name = "category") Category category,
                                                              HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        List<CommunityDTO> communityDTOList;
        if (category.equals(Category.ALL)) {
            if (range.equals(Range.LINE))
                communityDTOList = CommunityDTO.entityListToDTOList(communityService.findAllMyLine(page, count, accountId));
            else
                communityDTOList = CommunityDTO.entityListToDTOList(communityService.findAll(page, count, accountId));
        } else {
            if (range.equals(Range.LINE))
                communityDTOList = CommunityDTO.entityListToDTOList(communityService.findMyLineByCategory(category, page, count, accountId));
            else
                communityDTOList = CommunityDTO.entityListToDTOList(communityService.findByCategory(category, page, count, accountId));
        }
        return ApiUtil.success(new CommunityListDTO(communityService.isFirstPage(page),
                communityService.isLastPage(page, count),
                communityDTOList));
    }

    @ProfileRequired
    @Operation(summary = "내 커뮤니티글 전체 조회")
    @GetMapping("/api/v1/communities/me")
    public ApiUtil.ApiSuccessResult<CommunityListDTO> findAllMine(@RequestParam(name = "page") int page,
                                                                    @RequestParam(name = "count") int count,
                                                                    HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        return ApiUtil.success(new CommunityListDTO(communityService.isFirstPage(page),
                communityService.isLastPage(page, count),
                CommunityDTO.entityListToDTOList(communityService.findAllMine(page, count, profileId))));
    }

    @AccountRequired
    @Operation(summary = "커뮤니티글 상세 조회 (id)")
    @GetMapping("/api/v1/communities/{id}")
    public ApiSuccessResult<CommunityDTO> findById(@PathVariable(name = "id") Long id,
                                                   HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(new CommunityDTO(communityService.findCommunityById(id, accountId)));
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티글 수정")
    @PutMapping("/api/v1/communities/{id}")
    public ApiSuccessResult<Long> updateById(@PathVariable Long id,
                                             @Valid @RequestBody CommunityDTO communityDTO,
                                             HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        communityService.updateCommunity(id, communityDTO, profileId);
        return ApiUtil.success(id);
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티글 좋아요")
    @PutMapping("/api/v1/communities/{id}/like")
    public ApiSuccessResult<String> updateLike(@PathVariable Long id,
                                               HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        communityService.updateLike(id, profileId);
        return ApiUtil.success("좋아요가 업데이트 되었습니다.");
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티글 삭제")
    @DeleteMapping("/api/v1/communities/{id}")
    public ApiSuccessResult<String> deleteById(@PathVariable(name = "id") Long id,
                                               HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        communityService.delete(id, profileId);
        return ApiUtil.success("삭제되었습니다.");
    }
}
