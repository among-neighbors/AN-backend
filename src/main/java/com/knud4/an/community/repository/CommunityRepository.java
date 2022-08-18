package com.knud4.an.community.repository;

import com.knud4.an.community.entity.Range;
import com.knud4.an.community.entity.Category;
import com.knud4.an.community.entity.Community;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommunityRepository {

    private final EntityManager em;

    public void save(Community community) {
        em.persist(community);
    }

    public Community findOne(Long id) {
        return em.find(Community.class, id);
    }

    public List<Community> findAll() {
        return em.createQuery("select c from Community c", Community.class).getResultList();
    }

    public List<Community> findByLine(Long lineId) {
        return em.createQuery("select c from Community c where c.writer.account.line = :lineId", Community.class)
                .setParameter("lineId", lineId)
                .getResultList();
    }

    public List<Community> findByCategory(Category category) {
        return em.createQuery("select c from Community c where c.category = :category", Community.class)
                .setParameter("category", category)
                .getResultList();
    }

    public List<Community> findWithinPeriod(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select c from Community c where c.createdDate >= :from and c.createdDate <= :to", Community.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
}
