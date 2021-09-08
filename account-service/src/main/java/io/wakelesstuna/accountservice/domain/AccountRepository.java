package io.wakelesstuna.accountservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    Account update(Account account);
    List<Account> findAll();
    List<Account> findAllByHolderId(UUID id);
    Optional<Account> findById(UUID id);
    Optional<Account> findByHolderId(UUID id);
}
