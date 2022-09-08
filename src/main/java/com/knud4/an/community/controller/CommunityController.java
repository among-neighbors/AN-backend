package com.knud4.an.community.controller;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.board.Scope;
import com.knud4.an.community.dto.CommunityDTO;
import com.knud4.an.community.dto.CommunityListDTO;
import com.knud4.an.community.dto.CreateCommunityForm;
import com.knud4.an.community.entity.Category;
import com.knud4.an.community.entity.Community;
import com.knud4.an.community.service.CommunityService;
import com.knud4.an.annotation.AccountRequired;
import com.knud4.an.annotation.ProfileRequired;
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

@Tag(name = "커뮤니티")
@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    private final AccountService accountService;

    @ProfileRequired
    @Operation(summary = "커뮤니티글 생성", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티글 생성 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "404", description = "프로필이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PostMapping("/api/v1/communities")
    public ApiUtil.ApiSuccessResult<Long> createCommunity(@Valid @RequestBody CreateCommunityForm form,
                                                          HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        Profile profile = accountService.findProfileById(profileId);
        Long communityId = communityService.createCommunity(form, profile);
        return ApiUtil.success(communityId);
    }

    @AccountRequired
    @Operation(summary = "커뮤니티글 전체 조회", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티글 생성 성공", content = @Content(schema = @Schema(implementation = CommunityListDTO.class))),
            @ApiResponse(responseCode = "400", description = "게시글 요청 범위를 초과하였습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/communities")
    public ApiUtil.ApiSuccessResult<CommunityListDTO> findAll(@RequestParam(name = "page") int page,
                                                              @RequestParam(name = "count") int count,
                                                              @RequestParam(name = "scope") Scope scope,
                                                              @RequestParam(name = "category") Category category,
                                                              HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        return ApiUtil.success(new CommunityListDTO(communityService.isFirstPage(page),
                communityService.isLastPage(page, count),
                CommunityDTO.entityListToDTOList(communityService.findAll(scope, category, page, count, accountId))));
    }

    @ProfileRequired
    @Operation(summary = "내 커뮤니티글 전체 조회", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 커뮤니티글 전체 조회 성공", content = @Content(schema = @Schema(implementation = CommunityListDTO.class))),
            @ApiResponse(responseCode = "400", description = "게시글 요청 범위를 초과하였습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
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
    @Operation(summary = "커뮤니티글 상세 조회 (id)", description = "account token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티글 상세 조회 성공", content = @Content(schema = @Schema(implementation = CommunityDTO.class))),
            @ApiResponse(responseCode = "400", description = "접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "커뮤니티글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @GetMapping("/api/v1/communities/{id}")
    public ApiSuccessResult<CommunityDTO> findById(@PathVariable(name = "id") Long id,
                                                   HttpServletRequest req) {
        Long accountId = (Long) req.getAttribute("accountId");
        Community community = communityService.findCommunityById(id, accountId);
        CommunityDTO communityDTO = new CommunityDTO(community);
        communityDTO.setIsMine(communityService.isMine(community, accountId));
        return ApiUtil.success(communityDTO);
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티글 수정", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티글 수정 성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "수정 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "커뮤니티글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PutMapping("/api/v1/communities/{id}")
    public ApiSuccessResult<Long> updateById(@PathVariable Long id,
                                             @Valid @RequestBody CommunityDTO communityDTO,
                                             HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        communityService.updateCommunity(id, communityDTO, profileId);
        return ApiUtil.success(id);
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티글 좋아요", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티글 좋아요 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "커뮤니티글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @PutMapping("/api/v1/communities/{id}/like")
    public ApiSuccessResult<String> updateLike(@PathVariable Long id) {
        communityService.updateLike(id);
        return ApiUtil.success("좋아요가 업데이트 되었습니다.");
    }

    @ProfileRequired
    @Operation(summary = "커뮤니티글 삭제", description = "profile token이 필요합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "커뮤니티글 삭제 성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "삭제 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class))),
            @ApiResponse(responseCode = "404", description = "커뮤니티글이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiErrorResult.class)))
    })
    @DeleteMapping("/api/v1/communities/{id}")
    public ApiSuccessResult<String> deleteById(@PathVariable(name = "id") Long id,
                                               HttpServletRequest req) {
        Long profileId = (Long) req.getAttribute("profileId");
        communityService.delete(id, profileId);
        return ApiUtil.success("삭제되었습니다.");
    }
}
