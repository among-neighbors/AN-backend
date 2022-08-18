package com.knud4.an.notice.repository;

import com.knud4.an.notice.entity.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeRepository {

    private EntityManager em;

    public void save(Notice notice) {
        em.persist(notice);
    }

    public void remove(Notice notice) {
        em.remove(notice);
    }

    public Notice findOne(Long id) {
        return em.find(Notice.class, id);
    }

    public List<Notice> findAll() {
        return em.createQuery("select n from Notice n", Notice.class).getResultList();
    }

    public List<Notice> findBeforeExpire(LocalDateTime expiredDate) {
        return em.createQuery("select n from Notice n where n.expiredDate <= :expiredDate", Notice.class)
                .setParameter("expiredDate", expiredDate)
                .getResultList();
    }

    public List<Notice> findByLine(String lineId) {
        return em.createQuery("select n from Notice n where n.releaseLine = :lineId", Notice.class)
                .setParameter("lineId", lineId)
                .getResultList();
    }

    public List<Notice> findWithinPeriod(LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select n from Notice n where n.createdDate >= :from and n.createdDate <= :to", Notice.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

}
