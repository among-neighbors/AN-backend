package com.knud4.an.repository;

import com.knud4.an.account.entity.Account;
import com.knud4.an.community.entity.Community;
import com.knud4.an.community.repository.CommunityRepository;
import com.knud4.an.line.entity.Line;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class CommunityRepositoryTest {

    @Autowired
    CommunityRepository communityRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 페이징_조회() throws Exception {
        // given
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Line line = Line.builder().name("line" + i).build();
            Account account = Account.builder().line(line).build();
            Community community = Community.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .account(account)
                    .build();
            communityRepository.save(community);
        });

        // when
        List<Community> communities = communityRepository.findAll(2, 10);

        // then
        for(Community c : communities) System.out.println(c.getId() + " / " + c.getTitle() + " / " + c.getContent());
    }

    @Test
    public void 내_라인_게시물_조회() throws Exception {
        // given
        Line line = Line.builder().name("line10").build();
        Account account = Account.builder().line(line).build();
        em.persist(line);
        em.persist(account);

        // when
        List<Community> communities = communityRepository.findByLine(account.getLine().getName(), 1, 100);

        // then
        for(Community c : communities) System.out.println(c.getId() + " / " + c.getTitle() + " / " + c.getContent());
    }
}
