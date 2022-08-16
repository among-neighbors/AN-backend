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

    private EntityManager em;

    public void create(Community community) {
        em.persist(community);
    }

    public Community findOne(Long id) {
        return em.find(Community.class, id);
    }

    public List<Community> findAll() {
        return em.createQuery("select c from Community c", Community.class).getResultList();
    }

    public List<Community> findByRange(Range range) {
        return em.createQuery("select c from Community c where c.range = :range", Community.class)
                .setParameter("range", range)
                .getResultList();
    }

    public List<Community> findByCategory(Category category) {
        return em.createQuery("select c from Community c where c.category = :category", Community.class)
                .setParameter("category", category)
                .getResultList();
    }

    // 특정 날짜 게시물 vs 특정 날짜 이후 게시물 논의 필요
    public List<Community> findAfterCreatedDate(LocalDateTime createdDate) {
        return em.createQuery("select n from Notice n where n.createdDate >= :createdDate", Community.class)
                .setParameter("createdDate", createdDate)
                .getResultList();
    }
}
