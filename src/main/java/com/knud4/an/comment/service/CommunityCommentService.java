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
        Community community = communityRepository.findById(form.getBoardId())
                .orElseThrow(() -> new NotFoundException("커뮤니티글을 찾을 수 없습니다."));
        CommunityComment comment = CommunityComment.builder()
                .writer(writer)
                .text(form.getText())
                .community(community)
                .build();
        commentRepository.save(comment);
        return comment.getId();
    }

    public List<CommunityComment> findAllByCommunityId(int page, int count, Long communityId) {
        validatePaging(page, count, communityId);
        communityRepository.findById(communityId)
                .orElseThrow(() -> new NotFoundException("커뮤니티글을 찾을 수 없습니다."));
        return commentRepository.findAllByCommunityId(communityId, page, count);
    }

    @Transactional
    public void deleteById(Long id) {
        commentRepository.delete(
                commentRepository.findById(id).orElseThrow(() -> new NotFoundException("댓글이 존재하지 않습니다."))
        );
    }

    public Boolean isLastPage(int page, int cnt, Long communityId) {
        validatePaging(page, cnt, communityId);
        Long commentCnt = commentRepository.findCommentCountByCommunityId(communityId);
        return (long) page*cnt >= commentCnt;
    }

    public Boolean isFirstPage(int page) {
        return page == 1;
    }

    private void validatePaging(int page, int cnt, Long communityId) {
        Long num = commentRepository.findCommentCountByCommunityId(communityId);
        int limit = (page - 1) * cnt;
        if (page != 1 && num<=limit) {
            throw new IllegalStateException("댓글 요청 범위를 초과하였습니다.");
        }
    }

}
