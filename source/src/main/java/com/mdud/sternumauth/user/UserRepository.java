package com.mdud.sternumauth.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findDistinctByUsername(String username);
    Optional<User> findDistinctByEmail(String email);
}
