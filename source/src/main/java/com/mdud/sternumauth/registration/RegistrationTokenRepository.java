package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegistrationTokenRepository extends CrudRepository<RegistrationToken, Long> {
    Optional<RegistrationToken> findDistinctByToken(String token);
    Optional<RegistrationToken> findDistinctByUser(User user);
}
