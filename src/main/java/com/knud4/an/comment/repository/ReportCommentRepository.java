package com.knud4.an.comment.repository;

import com.knud4.an.comment.entity.ReportComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReportCommentRepository {

    private final EntityManager em;

    public void save(ReportComment comment) {
        em.persist(comment);
    }

    public Optional<ReportComment> findById(Long id) {
        return Optional.ofNullable(em.find(ReportComment.class, id));
    }

    public List<ReportComment> findAllByReportId(Long reportId, int page, int count) {
        return em.createQuery("select r from ReportComment r where r.report.id = :reportId", ReportComment.class)
                .setParameter("reportId", reportId)
                .setFirstResult((page-1)*count)
                .setMaxResults(count)
                .getResultList();
    }

    public Long findCommentCountByReportId(Long reportId) {
        return em.createQuery("select count(r) from ReportComment r where r.report.id = :reportId", Long.class)
                .setParameter("reportId", reportId)
                .getSingleResult();
    }

    public void delete(ReportComment comment) {
        em.remove(comment);
    }
}
