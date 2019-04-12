package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.AuthorityType;
import com.mdud.sternumauth.user.User;
import com.mdud.sternumauth.user.UserAuthority;
import com.mdud.sternumauth.user.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class RegistrationTokenMappingIT {

    @Autowired
    private RegistrationTokenRepository registrationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save_SaveTokenAndNewUser_ShouldSaveUserAndToken() {
        User newUser = new User.UserBuilder()
                .username("test")
                .password("pass")
                .email("email")
                .imageLink("")
                .addAuthority(AuthorityType.USER)
                .addAuthority(AuthorityType.MANAGER)
                .addAuthority(AuthorityType.ADMIN)
                .createUser();

        RegistrationToken registrationToken = new RegistrationToken(newUser);

        registrationToken = registrationTokenRepository.save(registrationToken);

        assertNotNull("saved token should have id", registrationToken.getId());
        assertNotNull("user saved with token should be persisted too", registrationToken.getUser().getId());
        assertThat("user authorities should be saved", registrationToken.getUser().getAuthoritySet().stream()
                        .map(UserAuthority::getId).collect(Collectors.toSet()),
                CoreMatchers.everyItem(CoreMatchers.notNullValue()));
    }

    @Test
    public void save_AddTokenToExistingUser_ShouldAddTokenToExistingUser() {
        User newUser = new User.UserBuilder()
                .username("test")
                .password("pass")
                .email("email")
                .imageLink("")
                .addAuthority(AuthorityType.USER)
                .addAuthority(AuthorityType.MANAGER)
                .addAuthority(AuthorityType.ADMIN)
                .createUser();

        long savedId = userRepository.save(newUser).getId();

        RegistrationToken registrationToken = new RegistrationToken(newUser);
        registrationToken = registrationTokenRepository.save(registrationToken);

        assertEquals("saving token to user should not change user", savedId,
                registrationToken.getUser().getId().longValue());
    }

    @Test
    public void delete_SaveTokenAndNewUserThenDeleteToken_ShouldNotDeleteUser() {
        User newUser = new User.UserBuilder()
                .username("test")
                .password("pass")
                .email("email")
                .imageLink("")
                .addAuthority(AuthorityType.USER)
                .addAuthority(AuthorityType.MANAGER)
                .addAuthority(AuthorityType.ADMIN)
                .createUser();

        RegistrationToken registrationToken = new RegistrationToken(newUser);
        registrationToken = registrationTokenRepository.save(registrationToken);
        registrationTokenRepository.delete(registrationToken);

        assertTrue("deleting token should not delete user", userRepository.findDistinctByUsername(newUser.getUsername()).isPresent());
    }

}