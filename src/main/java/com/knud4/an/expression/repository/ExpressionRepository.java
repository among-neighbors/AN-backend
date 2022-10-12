package com.knud4.an.expression.repository;

import com.knud4.an.expression.entity.Expression;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpressionRepository {

    private final EntityManager em;

    public void save(Expression expression) {
        em.persist(expression);
    }

    public void delete(Expression expression) {
        em.remove(expression);
    }

    public Optional<Expression> findByBoardIdnProfileId(Long boardId, Long profileId) {
        List<Expression> expressionList = em.createQuery(
                "select e from Expression e " +
                        "where e.board.id = :boardId " +
                        "and e.profile.id = :profileId",
                Expression.class)
                .setParameter("boardId", boardId)
                .setParameter("profileId", profileId)
                .getResultList();
        if(expressionList.isEmpty()) return Optional.empty();
        return Optional.of(expressionList.get(0));
    }

    public List<Expression> findByBoardId(Long boardId) {
        return em.createQuery("select e from Expression e " +
                                "where e.board.id = :boardId",
                        Expression.class)
                .setParameter("boardId", boardId)
                .getResultList();
    }

}
