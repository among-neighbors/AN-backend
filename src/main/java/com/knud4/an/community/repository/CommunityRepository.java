package com.knud4.an.community.repository;

import com.knud4.an.community.entity.Category;
import com.knud4.an.community.entity.Community;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommunityRepository {

    private final EntityManager em;

    public void save(Community community) {
        em.persist(community);
    }

    public Optional<Community> findById(Long id) {
        return Optional.ofNullable(em.find(Community.class, id));
    }


    public List<Community> findAllForAllWithCategory(Category category, String lineName, int page, int count, boolean desc) {
        String query = "select c from Community c where c.category = :category " +
                "and (c.scope = 'ALL' or (c.scope = 'LINE' and c.writerLineName = :lineName)) " +
                "order by c.id";
        if(desc) query += " desc";
        return em.createQuery(query, Community.class)
                .setParameter("category", category)
                .setParameter("lineName", lineName)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findAllForAll(String lineName, int page, int count, boolean desc) {
        String query = "select c from Community c " +
                "where (c.scope = 'ALL' or (c.scope = 'LINE' and c.writerLineName = :lineName)) " +
                "order by c.id";
        if(desc) query += " desc";
        return em.createQuery(query, Community.class)
                .setParameter("lineName", lineName)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findAllForLineWithCategory(Category category, String lineName, int page, int count, boolean desc) {
        String query = "select c from Community c where c.category = :category " +
                "and c.scope = 'LINE' and c.writerLineName = :lineName) " +
                "order by c.id";
        if(desc) query += " desc";
        return em.createQuery(query, Community.class)
                .setParameter("category", category)
                .setParameter("lineName", lineName)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findAllForLine(String lineName, int page, int count, boolean desc) {
        String query = "select c from Community c " +
                "where c.scope = 'LINE' and c.writerLineName = :lineName) " +
                "order by c.id";
        if(desc) query += " desc";
        return em.createQuery(query, Community.class)
                .setParameter("lineName", lineName)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findAllMine(Long profileId, int page, int count, boolean desc) {
        String query = "select c from Community c where c.writer.id = :profileId";
        if(desc) query += " desc";
        return em.createQuery(query, Community.class)
                .setParameter("profileId", profileId)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findWithinPeriod(LocalDateTime from, LocalDateTime to, int page, int count) {
        return em.createQuery("select c from Community c where c.createdDate >= :from and c.createdDate <= :to order by c.id desc", Community.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long findCommunityCount() {
        return em.createQuery("select count(c) from Community c", Long.class)
                .getSingleResult();
    }

    public void delete(Community community) {
        em.remove(community);
    }
}
