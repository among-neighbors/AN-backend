package com.knud4.an.comment.repository;

import com.knud4.an.comment.entity.CommunityComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommunityCommentRepository {

    private final EntityManager em;

    public void save(CommunityComment comment) {
        em.persist(comment);
    }

    public Optional<CommunityComment> findById(Long id) {
        return Optional.ofNullable(em.find(CommunityComment.class, id));
    }

    public List<CommunityComment> findAllByCommunityId(Long communityId) {
        return em.createQuery("select c from CommunityComment c where c.community.id = :communityId order by c.id desc", CommunityComment.class)
                .setParameter("communityId", communityId)
                .getResultList();
    }

    public List<CommunityComment> findAllByCommunityId(Long communityId, int page, int count) {
        return em.createQuery("select c from CommunityComment c where c.community.id = :communityId order by c.id desc", CommunityComment.class)
                .setParameter("communityId", communityId)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long findCommentCountByCommunityId(Long communityId) {
        return em.createQuery("select count(c) from CommunityComment c where c.community.id = :communityId", Long.class)
                .setParameter("communityId", communityId)
                .getSingleResult();
    }

    public void delete(CommunityComment comment) {
        em.remove(comment);
    }
}
