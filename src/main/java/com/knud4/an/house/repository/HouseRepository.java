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
    private final LineRepository lineRepository;

    public void save(House house) {
        em.persist(house);
    }

    public void updateHouseName(String lineName, String oldName, String updateName) {
        House findHouse = findByName(lineName, oldName);

        findHouse.changeName(updateName);
    }

    public House findByName(String lineName, String houseName) {
        Line findLine = lineRepository.findByName(lineName);
        return em.createQuery("select h from House h " +
                        "where h.line = :line and h.name = :name", House.class)
                .setParameter("line", findLine)
                .setParameter("name", houseName)
                .getSingleResult();
    }

    public List<House> findHousesByLineName(String lineName) {
        Line findLine = lineRepository.findByName(lineName);
        return em.createQuery("select h from House h where h.line = :line", House.class)
                .setParameter("line", findLine)
                .getResultList();
    }

    public void deleteByName(String lineName, String houseName) {
        Line findLine = lineRepository.findByName(lineName);
        em.createQuery("delete from House h where " +
                "h.line = :line and h.name = :name")
                .setParameter("line", findLine)
                .setParameter("name", houseName)
                .executeUpdate();
    }

    public List<House> findAll() {
        return em.createQuery("select h from House h", House.class)
                .getResultList();
    }
}
