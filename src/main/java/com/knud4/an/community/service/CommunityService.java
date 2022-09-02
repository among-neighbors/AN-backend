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
import com.knud4.an.board.Range;
import com.knud4.an.community.repository.CommunityRepository;
import com.knud4.an.exception.NotAuthenticatedException;
import com.knud4.an.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .range(form.getRange())
                .likes(0L)
                .build();
        communityRepository.save(community);
        return community.getId();
    }

    public Community findCommunityById(Long communityId, Long accountId) {
        Community findCommunity = communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException("커뮤니티글을 찾을 수 없습니다."));
        if(findCommunity.getRange() == Range.LINE) {
            Account account = accountRepository.findAccountById(accountId);
            if(!findCommunity.getWriterLineName().equals(account.getLine().getName())) {
                throw new NotAuthenticatedException("접근 권한이 없습니다.");
            }
        }
        return findCommunity;
    }

    public List<Community> findAll(int page, int count, Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if(account.getRole() == Role.ROLE_MANAGER) return communityRepository.findAll(page, count);
        List<Community> findCommunities = communityRepository.findByRange(Range.ALL, page, count);
        findCommunities.addAll(communityRepository.findByRangeAndLine(Range.LINE, account.getLine().getName(), page, count));
        return findCommunities;
    }

    public List<Community> findAllMyLine(int page, int count, Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        return communityRepository.findByLine(account.getLine().getName(), page, count);
    }

    public List<Community> findByCategory(Category category, int page, int count, Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if(account.getRole().equals(Role.ROLE_MANAGER)) {
            return communityRepository.findByCategory(category, page, count);
        }
        List<Community> findCommunities = communityRepository.findByRangeAndCategory(Range.ALL, category, page, count);
        findCommunities.addAll(communityRepository.findByRangeAndLineAndCategory(Range.LINE, account.getLine().getName(), category, page, count));
        return findCommunities;
    }

    public List<Community> findMyLineByCategory(Category category, int page, int count, Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        return communityRepository.findByLineAndCategory(account.getLine().getName(), category, page, count);
    }

    public List<Community> findAllMine(int page, int count, Long profileId) {
        return communityRepository.findAllMine(profileId, page, count);
    }

    public Boolean isFirstPage(int page) {
        return page == 1;
    }

    public Boolean isLastPage(int page, int count) {
        Long communityCnt = communityRepository.findCommunityCount();
        return (long) (page + 2) * count >= communityCnt;
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
        community.changeRange(communityDTO.getRange());
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
}