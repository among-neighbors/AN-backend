package com.knud4.an.comment.repository;

import com.knud4.an.comment.entity.ReportComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportCommentRepository {

    private final EntityManager em;

    public void save(ReportComment comment) {
        em.persist(comment);
    }

    public List<ReportComment> findAllByReportId(Long reportId, int page, int count) {
        return em.createQuery("select r from ReportComment r where r.report = :reportId", ReportComment.class)
                .setParameter("reportId", reportId)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long findCountByReportId(Long reportId) {
        return em.createQuery("select count(r) from ReportComment r where r.report = :reportId", Long.class)
                .setParameter("reportId", reportId)
                .getSingleResult();
    }
}