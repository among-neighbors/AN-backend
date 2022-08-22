package com.knud4.an.comment.repository;

import com.knud4.an.comment.entity.CommunityComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommunityCommentRepository {

    private final EntityManager em;

    public void save(CommunityComment comment) {
        em.persist(comment);
    }

    public List<CommunityComment> findAllByCommunityId(Long communityId, int page, int count) {
        return em.createQuery("select c from CommunityComment c where c.community = :communityId", CommunityComment.class)
                .setParameter("communityId", communityId)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long findCommentCountByCommunityId(Long communityId) {
        return em.createQuery("select count(c) from CommunityComment c where c.community = :communityId", Long.class)
                .setParameter("communityId", communityId)
                .getSingleResult();
    }
}
