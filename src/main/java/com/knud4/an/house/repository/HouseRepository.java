package com.knud4.an.house.repository;

import com.knud4.an.house.entity.House;
import com.knud4.an.house.entity.House;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HouseRepository {

    private final EntityManager em;

    public void save(House house) {
        em.persist(house);
    }

    public void updateHouseName(Long lineId, String oldName, String updateName) {
        House findHouse = findByName(lineId, oldName);

        findHouse.changeName(updateName);
    }

    public House findByName(Long lineId, String houseName) {
        return em.createQuery("select h from House h " +
                        "where h.line.id = :lineId and h.name = :name", House.class)
                .setParameter("lineId", lineId)
                .setParameter("name", houseName)
                .getSingleResult();
    }

    public List<House> findHousesByLineName(Long lineId) {
        return em.createQuery("select h from House h where h.line.id = :lineId", House.class)
                .setParameter("lineId", lineId)
                .getResultList();
    }

    public void deleteByName(Long lineId, String houseName) {
        em.createQuery("delete from House h where " +
                "h.line.id = :lineId and h.name = :name")
                .setParameter("lineId", lineId)
                .setParameter("name", houseName)
                .executeUpdate();
    }

    public List<House> findAll() {
        return em.createQuery("select h from House h", House.class)
                .getResultList();
    }
}
