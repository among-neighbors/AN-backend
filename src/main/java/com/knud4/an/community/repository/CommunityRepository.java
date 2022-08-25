package com.knud4.an.community.repository;

import com.knud4.an.board.Range;
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

    public List<Community> findAll(int page, int count) {
        return em.createQuery("select c from Community c order by c.id desc", Community.class)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findByLine(String lineName, int page, int count) {
        return em.createQuery("select c from Community c where c.writerLineName = :lineName order by c.id desc", Community.class)
                .setParameter("lineName", lineName)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findAllMine(Long profileId, int page, int count) {
        return em.createQuery("select c from Community c where c.writer.id = :profileId", Community.class)
                .setParameter("profileId", profileId)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findByRange(Range range, int page, int count) {
        return em.createQuery("select c from Community c where c.range = :range order by c.id desc", Community.class)
                .setParameter("range", range)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findByRangeAndLine(Range range, String lineName, int page, int count) {
        return em.createQuery("select c from Community c where c.range = :range and c.writerLineName = :lineName order by c.id desc", Community.class)
                .setParameter("range", range)
                .setParameter("lineName", lineName)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findByCategory(Category category, int page, int count) {
        return em.createQuery("select c from Community c where c.category = :category order by c.id desc", Community.class)
                .setParameter("category", category)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findByRangeAndCategory(Range range, Category category, int page, int count) {
        return em.createQuery("select c from Community c where c.range = :range and c.category = :category order by c.id desc", Community.class)
                .setParameter("range", range)
                .setParameter("category", category)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findByRangeAndLineAndCategory(Range range, String lineName, Category category, int page, int count) {
        return em.createQuery("select c from Community c where c.range = :range and c.writerLineName = :lineName and c.category = :category order by c.id desc", Community.class)
                .setParameter("range", range)
                .setParameter("lineName", lineName)
                .setParameter("category", category)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Community> findByLineAndCatetory(String lineName, Category category, int page, int count) {
        return em.createQuery("select c from Community c where c.writerLineName = :lineName and c.category = :category order by c.id desc", Community.class)
                .setParameter("lineName", lineName)
                .setParameter("category", category)
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
}
