package com.knud4.an.expression.service;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.community.entity.Community;
import com.knud4.an.community.repository.CommunityRepository;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.expression.dto.CreateExpressionForm;
import com.knud4.an.expression.entity.Expression;
import com.knud4.an.expression.repository.ExpressionRepository;
import com.knud4.an.utils.redis.RedisUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ExpressionService {

    private final ExpressionRepository expressionRepository;
    private final CommunityRepository communityRepository;
    private final AccountRepository accountRepository;
    private final RedisUtil redis;
    private static final String keyPrefix = "an:expression:";
    private static final long duration = 3600*24*100;

    @Transactional
    public void save(CreateExpressionForm form, Long profileId) {
        String key = keyPrefix + form.getCommunityId();
        if(!redis.exists(key)) DBtoCache(form.getCommunityId());

        if(redis.sIsMember(key, profileId)) {
            redis.sRem(key, profileId);
            Expression findEx = expressionRepository.findByBoardIdnProfileId(form.getCommunityId(), profileId)
                    .orElseThrow(() -> new IllegalStateException("비정상적인 데이터"));
            expressionRepository.delete(findEx);
        } else {
            redis.sAdd(key, profileId);
            redis.expire(key, duration);
            Community community = communityRepository.findById(form.getCommunityId())
                    .orElseThrow(() -> new NotFoundException("글이 존재하지 않습니다."));
            Profile profile = accountRepository.findProfileById(profileId)
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 프로필입니다."));
            Expression expression = Expression.builder()
                    .board(community)
                    .profile(profile)
                    .build();
            expressionRepository.save(expression);
        }
    }

    public Long findLikeCountByBoardId(Long boardId) {
        String key = keyPrefix + boardId;
        if(!redis.exists(key)) DBtoCache(boardId);
        return redis.sSize(key);
    }

    public Boolean didILiked(Long boardId, Long profileId) {
        String key = keyPrefix + boardId;
        if(!redis.exists(key)) DBtoCache(boardId);
        return redis.sIsMember(key, profileId);
    }

    public void DBtoCache(Long boardId) {
        String key = keyPrefix + boardId;
        List<Expression> expressionList = expressionRepository.findByBoardId(boardId);
        for(Expression expression : expressionList)
            redis.sAdd(keyPrefix + expression.getBoard().getId(), expression.getProfile().getId());
        redis.expire(key, duration);
    }
}
