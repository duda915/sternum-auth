package com.mdud.sternumauth.user;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class UserMappingIT {
    //feel like multiple asserts with comments in integration tests are not that bad

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save_SaveNewUserWithAuthorities_ShouldSaveUserAndAuthoritiesToDB() {
        HashSet<UserAuthority> authorities = new HashSet<>();
        Arrays.asList(AuthorityType.values()).forEach(authorityType -> authorities.add(new UserAuthority(authorityType)));

        String password = "password";
        User newUser = new User("name", "email", password, null, authorities);
        userRepository.save(newUser);

        User saved = userRepository.findDistinctByUsername(newUser.getUsername()).orElseThrow(() -> new AssertionError("cannot find saved entity"));

        assertNotNull("saved object should have id", saved.getId());
        assertThat("authorities saved with user should have ids", saved.getAuthoritySet(), CoreMatchers.everyItem(CoreMatchers.notNullValue(UserAuthority.class)));
    }

}