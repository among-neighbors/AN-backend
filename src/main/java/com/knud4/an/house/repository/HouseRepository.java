package com.knud4.an.house.repository;

import com.knud4.an.house.entity.House;
import com.knud4.an.house.entity.House;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HouseRepository {

    private final EntityManager em;

    public void save(House house) {
        em.persist(house);
    }

    public House findById(Long id) { return em.find(House.class, id); }

    public Optional<House> findByLineIdAndHouseName(Long lineId, String houseName) {
        return em.createQuery("select h from House h " +
                        "where h.line.id = :lineId and h.name = :name", House.class)
                .setParameter("lineId", lineId)
                .setParameter("name", houseName)
                .getResultList().stream()
                .findFirst();
    }

    public List<House> findHousesByLineId(Long lineId) {
        return em.createQuery("select h from House h where h.line.id = :lineId", House.class)
                .setParameter("lineId", lineId)
                .getResultList();
    }

    public List<House> findAll() {
        return em.createQuery("select h from House h", House.class)
                .getResultList();
    }

    public boolean existHouseByNameAndLineId(String houseName, Long lineId) {
        return em.createQuery("select count(h)>0 from House h " +
                "where h.line.id = :lineId and h.name = :houseName", Boolean.class)
                .setParameter("lineId", lineId)
                .setParameter("houseName", houseName)
                .getSingleResult();
    }
}
