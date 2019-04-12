package com.mdud.sternumauth.registration;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegistrationTokenRepository extends CrudRepository<RegistrationToken, Long> {
    Optional<RegistrationToken> findDistinctByToken(String token);
}
