package com.knud4.an.community.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.entity.Role;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.comment.entity.CommunityComment;
import com.knud4.an.comment.repository.CommunityCommentRepository;
import com.knud4.an.community.dto.CommunityDTO;
import com.knud4.an.community.dto.CreateCommunityForm;
import com.knud4.an.community.entity.Category;
import com.knud4.an.community.entity.Community;
import com.knud4.an.board.Scope;
import com.knud4.an.community.repository.CommunityRepository;
import com.knud4.an.exception.NotAuthenticatedException;
import com.knud4.an.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommunityRepository communityRepository;

    private final CommunityCommentRepository commentRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public Long createCommunity(CreateCommunityForm form, Profile writer) {
        Community community = Community.builder()
                .writer(writer)
                .title(form.getTitle())
                .content(form.getContent())
                .category(form.getCategory())
                .scope(form.getScope())
                .like(0L)
                .build();
        communityRepository.save(community);
        return community.getId();
    }

    public Community findCommunityById(Long communityId, Long accountId) {
        Community findCommunity = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException("커뮤니티글을 찾을 수 없습니다."));
        if(findCommunity.getScope() == Scope.LINE) {
            Account account = accountRepository.findAccountById(accountId);
            if(!findCommunity.getWriterLineName().equals(account.getLine().getName())) {
                throw new NotAuthenticatedException("접근 권한이 없습니다.");
            }
        }
        return findCommunity;
    }

    public List<Community> findAll(int page, int count, Long accountId) {
        validatePaging(page, count);
        Account account = accountRepository.findAccountById(accountId);
        if(account.getRole() == Role.ROLE_MANAGER) return communityRepository.findAll(page, count, true);
        List<Community> findCommunities = communityRepository.findByScope(Scope.ALL, page, count, false);
        findCommunities.addAll(communityRepository.findByScopeAndLine(Scope.LINE, account.getLine().getName(), page, count, false));
        findCommunities.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
        return findCommunities;
    }

    public List<Community> findAllMyLine(int page, int count, Long accountId) {
        validatePaging(page, count);
        Account account = accountRepository.findAccountById(accountId);
        return communityRepository.findByScopeAndLine(Scope.LINE, account.getLine().getName(), page, count, false);
    }

    public List<Community> findByCategory(Category category, int page, int count, Long accountId) {
        validatePaging(page, count);
        Account account = accountRepository.findAccountById(accountId);
        if(account.getRole().equals(Role.ROLE_MANAGER)) {
            return communityRepository.findByCategory(category, page, count, true);
        }
        List<Community> findCommunities = communityRepository.findByScopeAndCategory(Scope.ALL, category, page, count, false);
        findCommunities.addAll(communityRepository.findByScopeAndLineAndCategory(Scope.LINE, account.getLine().getName(), category, page, count, false));
        findCommunities.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
        return findCommunities;
    }

    public List<Community> findMyLineByCategory(Category category, int page, int count, Long accountId) {
        validatePaging(page, count);
        Account account = accountRepository.findAccountById(accountId);
        return communityRepository.findByLineAndCategory(account.getLine().getName(), category, page, count, true);
    }

    public List<Community> findAllMine(int page, int count, Long profileId) {
        validatePaging(page, count);
        return communityRepository.findAllMine(profileId, page, count, true);
    }

    public Boolean isLastPage(int page, int cnt) {
        validatePaging(page, cnt);
        Long communityCnt = communityRepository.findCommunityCount();
        return (long) page*cnt >= communityCnt;
    }

    public Boolean isFirstPage(int page) {
        return page == 1;
    }

    private void validatePaging(int page, int cnt) {
        Long num = communityRepository.findCommunityCount();
        int limit = (page - 1) * cnt;
        if (page != 1 && num<=limit) {
            throw new IllegalStateException("게시글 요청 범위를 초과하였습니다.");
        }
    }

    @Transactional
    public void updateCommunity(Long id, CommunityDTO communityDTO, Long profileId) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("커뮤니티 글이 존재하지 않습니다."));
        if(!community.getWriter().getId().equals(profileId))
            throw new NotAuthenticatedException("수정 권한이 없습니다.");
        community.changeTitle(communityDTO.getTitle());
        community.changeContent(communityDTO.getContent());
        community.changeCategory(communityDTO.getCategory());
        community.changeScope(communityDTO.getScope());
    }

    @Transactional
    public void updateLike(Long communityId, Long profileId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException("커뮤니티 글이 존재하지 않습니다."));
        if(accountRepository.findProfileById(profileId) == null)
            throw new NotAuthenticatedException("권한이 없습니다.");
        community.increaseLike();
    }

    @Transactional
    public void delete(Long id, Long profileId) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("커뮤니티 글이 존재하지 않습니다."));
        Profile profile = accountRepository.findProfileById(profileId);

        if(!community.getWriter().equals(profile))
            throw new NotAuthenticatedException("삭제 권한이 없습니다.");

        List<CommunityComment> comments = commentRepository.findAllByCommunityId(id);
        for(CommunityComment comment : comments) commentRepository.delete(comment);
        communityRepository.delete(community);
    }

    public Boolean isMine(Community community, Long accountId) {
        return Objects.equals(community.getWriter().getAccount().getId(), accountId);
    }
}