package io.wakelesstuna.accountservice.persistance;

import io.wakelesstuna.accountservice.domain.Account;
import io.wakelesstuna.accountservice.domain.AccountRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class AccountRepositoryImp implements AccountRepository {

    @PersistenceContext
    private final EntityManager em;

    public AccountRepositoryImp(EntityManager em) {
        this.em = em;
    }

    @Override
    public Account save(Account account) {
        em.persist(account);
        return account;
    }

    @Override
    public Account update(Account account) {
        return em.merge(account);
    }

    @Override
    public List<Account> findAll() {
        TypedQuery<Account> q = em.createQuery("select a from Account a", Account.class);
        return q.getResultList();
    }

    @Override
    public List<Account> findAllByHolderId(UUID id) {
        TypedQuery<Account> q = em.createQuery("select a from Account a where a.holderId = :id", Account.class);
        q.setParameter("id", id);
        return q.getResultList();
    }

    @Override
    public Optional<Account> findById(UUID id) {
        TypedQuery<Account> q = em.createQuery("select a from Account a where a.id = :id", Account.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }

    @Override
    public Optional<Account> findByHolderId(UUID id) {
        TypedQuery<Account> q = em.createQuery("select a from Account a where a.holderId = :id", Account.class);
        q.setParameter("id", id);
        return Optional.ofNullable(q.getSingleResult());
    }
}
