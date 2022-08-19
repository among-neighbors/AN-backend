package com.knud4.an.community.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    @Transactional
    public Long createCommunity(CreateCommunityForm form, Profile writer, Account account) {
        Community community = Community.builder()
                .writer(writer)
                .title(form.getTitle())
                .content(form.getContent())
                .category(form.getCategory())
                .range(form.getRange())
                .likes(0L)
                .account(account)
                .build();
        communityRepository.save(community);
        return community.getId();
    }

    public Community findCommunityById(Long communityId, Long accountId) {
        Community findCommunity = communityRepository.findOne(communityId);
        if(findCommunity == null) {
            throw new NotFoundException("커뮤니티글을 찾을 수 없습니다.");
        }
        if(findCommunity.getRange() == Range.LINE) {
            Account account = accountRepository.findAccountById(accountId);
            if(!findCommunity.getWriterLine().equals(account.getLine().getName())) {
                throw new NotAuthenticatedException("접근 권한이 없습니다.");
            }
        }
        return findCommunity;
    }

    public List<Community> findAll(int page, int count, Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
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
        List<Community> findCommunities = communityRepository.findByRangeAndCategory(Range.ALL, category, page, count);
        findCommunities.addAll(communityRepository.findByRangeAndLineAndCategory(Range.LINE, account.getLine().getName(), category, page, count));
        return findCommunities;
    }

    public List<Community> findMyLineByCategory(Category category, int page, int count, Long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        return communityRepository.findByLineAndCatetory(account.getLine().getName(), category, page, count);
    }
}
