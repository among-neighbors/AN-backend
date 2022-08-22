package com.knud4.an.comment.service;

import com.knud4.an.account.entity.Profile;
import com.knud4.an.comment.dto.CreateCommentForm;
import com.knud4.an.comment.entity.CommunityComment;
import com.knud4.an.comment.repository.CommunityCommentRepository;
import com.knud4.an.community.entity.Community;
import com.knud4.an.community.repository.CommunityRepository;
import com.knud4.an.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityCommentService {

    private final CommunityCommentRepository commentRepository;
    private final CommunityRepository communityRepository;

    @Transactional
    public Long createCommunityComment(CreateCommentForm form, Profile writer) {
        Community community = communityRepository.findOne(form.getBoardId());
        if(community == null) {
            throw new NotFoundException("커뮤니티글을 찾을 수 없습니다.");
        }
        CommunityComment comment = CommunityComment.builder()
                .writer(writer)
                .text(form.getText())
                .community(community)
                .build();
        commentRepository.save(comment);
        return comment.getId();
    }

    public List<CommunityComment> findAllByCommunityId(int page, int count, Long communityId) {
        return commentRepository.findAllByCommunityId(communityId, page, count);
    }
}