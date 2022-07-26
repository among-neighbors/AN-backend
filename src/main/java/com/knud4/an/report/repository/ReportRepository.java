package com.knud4.an.report.repository;

import com.knud4.an.report.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReportRepository {
    private final EntityManager em;

    public void save(Report report) { em.persist(report); }

    public Optional<Report> findById(Long id) { return Optional.ofNullable(em.find(Report.class, id)); }

    public List<Report> findAll() {
        return em.createQuery("select r from Report r", Report.class)
                .getResultList();
    }

    public List<Report> findAll(int offset, int limit) {
        return em.createQuery("select r from Report r order by r.createdDate desc", Report.class)
                .setFirstResult((offset-1)*limit)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Report> findByAccountId(Long accountId) {
        return em.createQuery("select r from Report r " +
                        "where r.writer.id = :writerId", Report.class)
                .setParameter("writerId", accountId)
                .getResultList();
    }

    public List<Report> findByAccountId(int offset, int limit, Long accountId) {
        return em.createQuery("select r from Report r " +
                        "where r.writer.id = :writerId order by r.id desc", Report.class)
                .setParameter("writerId", accountId)
                .setFirstResult((offset-1)*limit)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long countByAccountId(Long accountId) {
        return em.createQuery("select count(r) from Report r " +
                "where r.writer.id = :writerId", Long.class)
                .setParameter("writerId", accountId)
                .getSingleResult();
    }

    public Long countAll() {
        return em.createQuery("select count(r) from Report r", Long.class)
                .getSingleResult();
    }
}
