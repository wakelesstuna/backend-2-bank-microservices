package io.wakelesstuna.userservice.domain;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findRoleByName(String name);
    List<Role> findAll();
}
