package com.knud4.an.notice.repository;

import com.knud4.an.board.Range;
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

    public List<Notice> findByRange(Range range) {
        return em.createQuery("select n from Notice n where n.range = :range", Notice.class)
                .setParameter("range", range)
                .getResultList();
    }

    // 특정 날짜 게시물 vs 특정 날짜 이후 게시물 논의 필요
    public List<Notice> findAfterCreatedDate(LocalDateTime createdDate) {
        return em.createQuery("select n from Notice n where n.createdDate >= :createdDate", Notice.class)
                .setParameter("createdDate", createdDate)
                .getResultList();
    }

}
