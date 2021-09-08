package io.wakelesstuna.userservice.persistance;

import io.wakelesstuna.userservice.domain.Role;
import io.wakelesstuna.userservice.domain.RoleRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Transactional
public class RoleRepositoryImp implements RoleRepository {

    @PersistenceContext
    private final EntityManager em;

    public RoleRepositoryImp(EntityManager entityManager) {
        this.em = entityManager;
    }


    @Override
    public Role save(Role role) {

        em.persist(role);

        // TODO: 2021-09-08 hur gör man för att inte dubbel lagra information???
        /*Optional<Role> roleOpt = findRoleByName(role.getName());
        if (roleOpt.isEmpty()) {
            em.persist(role);
        }else {
            em.merge(role);
        }*/

        return role;
    }

    @Override
    public Optional<Role> findRoleByName(String name) {
        TypedQuery<Role> q = em.createQuery("select a from Role a where a.name = :name", Role.class);
        q.setParameter("name", name);
        return Optional.ofNullable(q.getSingleResult());
    }

    @Override
    public List<Role> findAll() {
        TypedQuery<Role> q = em.createQuery("select a from Role a", Role.class);
        return q.getResultList();
    }
}
