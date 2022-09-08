package com.knud4.an.notice.repository;

import com.knud4.an.board.Scope;
import com.knud4.an.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

    private final EntityManager em;

    public void save(Notice notice) {
        em.persist(notice);
    }

    public Optional<Notice> findById(Long id) {
        return Optional.ofNullable(em.find(Notice.class, id));
    }

    public List<Notice> findAll(int page, int count, boolean desc) {
        String query = "select n from Notice n order by n.id";
        if(desc) query += " desc";
        return em.createQuery(query, Notice.class)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Notice> findAllForAll(String lineName, int page, int count, boolean desc) {
        String query = "select n from Notice n " +
                "where n.scope = 'ALL' " +
                "or (n.scope = 'LINE' and n.releaseLine = :lineName) " +
                "order by n.id";
        if(desc) query += " desc";
        return em.createQuery(query, Notice.class)
                .setParameter("lineName", lineName)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long countAllForAll(String lineName) {
        return em.createQuery("select count(n) from Notice n " +
                "where n.scope = 'ALL' " +
                "or (n.scope = 'LINE' and n.releaseLine = :lineName)",
                Long.class)
                .setParameter("lineName", lineName)
                .getSingleResult();
    }

    public List<Notice> findAllForLine(String lineName, int page, int count, boolean desc) {
        String query = "select n from Notice n " +
                "where n.scope = 'LINE' and n.releaseLine = :lineName " +
                "order by n.id";
        if(desc) query += " desc";
        return em.createQuery(query, Notice.class)
                .setParameter("lineName", lineName)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long countAllForLine(String lineName) {
        return em.createQuery("select n from Notice n " +
                "where n.scope = 'LINE' and n.releaseLine = :lineName",
                Long.class)
                .setParameter("lineName", lineName)
                .getSingleResult();
    }

    public Long findNoticeCount() {
        return em.createQuery("select count(n) from Notice n", Long.class)
                .getSingleResult();
    }

    public void delete(Notice notice) {
        em.remove(notice);
    }
}
