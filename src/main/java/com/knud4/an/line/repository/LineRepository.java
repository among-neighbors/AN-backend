package com.knud4.an.line.repository;

import com.knud4.an.line.entity.Line;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LineRepository {

    private final EntityManager em;

    public void save(Line line) {
        em.persist(line);
    }

    public void updateName(String oldName, String updateName) {
        Line findLine = findByName(oldName);
        findLine.changeName(updateName);
    }

    public void deleteByName(String lineName) {
        em.createQuery("delete from Line l where l.name = :name")
                .setParameter("name", lineName).executeUpdate();
    }

    public Line findByName(String lineName) {
        return em.createQuery("select l from Line l where l.name = :name", Line.class)
                .setParameter("name", lineName)
                .getSingleResult();
    }

    public List<Line> findAll() {
        return em.createQuery("select l from Line l", Line.class).getResultList();
    }
}
