package io.wakelesstuna.userservice.persistance;

import io.wakelesstuna.userservice.domain.User;
import io.wakelesstuna.userservice.domain.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImp implements UserRepository {

    @PersistenceContext
    private final EntityManager em;

    public UserRepositoryImp(EntityManager em) {
        this.em = em;
    }

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        TypedQuery<User> q = em.createQuery("select a from User a where a.username = :username", User.class);
        q.setParameter("username", username);
        return Optional.ofNullable(q.getSingleResult());
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> q = em.createQuery("select a from User a", User.class);
        return q.getResultList();
    }
}
