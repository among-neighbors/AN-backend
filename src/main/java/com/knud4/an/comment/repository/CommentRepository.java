package com.knud4.an.comment.repository;

import com.knud4.an.comment.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    // 커뮤니티 글 아이디로 모든 댓글 조회 구현 필요
    public List<Comment> findAllByCommunityId(Long communityId) {
        return null;
    }

    public void delete(Comment comment) {
        em.remove(comment);
    }
}
