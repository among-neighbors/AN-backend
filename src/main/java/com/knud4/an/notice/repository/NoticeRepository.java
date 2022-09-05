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

    public List<Notice> findAll(int page, int count) {
        return em.createQuery("select n from Notice n order by n.id desc", Notice.class)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Notice> findBeforeExpire(LocalDateTime expiredDate, int page, int count) {
        return em.createQuery("select n from Notice n where n.expiredDate <= :expiredDate order by n.id desc", Notice.class)
                .setParameter("expiredDate", expiredDate)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Notice> findByLine(String releaseLine, int page, int count) {
        return em.createQuery("select n from Notice n where n.releaseLine = :releaseLine order by n.id desc", Notice.class)
                .setParameter("releaseLine", releaseLine)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Notice> findByScope(Scope scope, int page, int count) {
        return em.createQuery("select n from Notice n where n.scope = :scope order by n.id desc", Notice.class)
                .setParameter("scope", scope)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Notice> findByScopeAndLine(Scope scope, String releaseLine, int page, int count) {
        return em.createQuery("select n from Notice n where n.scope = :scope and n.releaseLine = :releaseLine order by n.id desc", Notice.class)
                .setParameter("scope", scope)
                .setParameter("releaseLine", releaseLine)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public List<Notice> findWithinPeriod(LocalDateTime from, LocalDateTime to, int page, int count) {
        return em.createQuery("select n from Notice n where n.createdDate >= :from and n.createdDate <= :to order by n.id desc", Notice.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long findNoticeCount() {
        return em.createQuery("select count(n) from Notice n", Long.class)
                .getSingleResult();
    }

    public void delete(Notice notice) {
        em.remove(notice);
    }
}
