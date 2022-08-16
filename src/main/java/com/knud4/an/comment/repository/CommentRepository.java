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

    public List<Comment> findAllByCommunityId(Long communityId) {
        return em.createQuery("select c from Comment c where c.community = :communityId", Comment.class)
                .setParameter("communityId", communityId)
                .getResultList();
    }

    public void delete(Comment comment) {
        em.remove(comment);
    }
}
