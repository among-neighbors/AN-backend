package com.knud4.an.account.repository;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final EntityManager em;

    public void saveAccount(Account account) {em.persist(account);}

    public void saveProfile(Profile profile) {em.persist(profile);}

    public Optional<Account> findAccountByEmail(String email) {
        return em.createQuery("select a from Account a " +
                "where a.email = :email", Account.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }

    public Optional<Account> findAccountByUsername(String username) {
        return em.createQuery("select a from Account a " +
                        "where a.username = :username", Account.class)
                .setParameter("username", username)
                .getResultList()
                .stream().findFirst();
    }


    public List<Profile> findProfilesByAccountId(Long accountId) {
        return em.createQuery("select p from Profile p " +
                "where p.account.id = :accountId", Profile.class)
                .setParameter("accountId", accountId)
                .getResultList();
    }

    public Optional<Profile> findProfileByAccountIDAndProfileName(Long accountId, String profileName) {
        return em.createQuery("select p from Profile p " +
                        "where p.name = :profileName " +
                        "and p.account.id = :accountId", Profile.class)
                .setParameter("profileName", profileName)
                .setParameter("accountId", accountId)
                .getResultList()
                .stream().findFirst();
    }

    public Optional<Profile> findProfileByID(Long profileId) {
        return em.createQuery("select p from Profile p " +
                        "where p.id = :profileId", Profile.class)
                .setParameter("profileId", profileId)
                .getResultList()
                .stream().findFirst();
    }


}
