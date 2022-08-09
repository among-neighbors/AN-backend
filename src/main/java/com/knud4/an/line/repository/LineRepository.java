package com.knud4.an.line.repository;

import com.knud4.an.line.entity.Line;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LineRepository {

    private final EntityManager em;

    public void save(Line line) {
        em.persist(line);
    }

    public Line findById(Long id) { return em.find(Line.class, id); }

    public Optional<Line> findByName(String lineName) {
        return em.createQuery("select l from Line l where l.name = :name", Line.class)
                .setParameter("name", lineName)
                .getResultList().stream()
                .findFirst();
    }

    public List<Line> findAll() {
        return em.createQuery("select l from Line l", Line.class).getResultList();
    }
}
