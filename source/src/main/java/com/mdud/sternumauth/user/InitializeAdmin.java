package com.mdud.sternumauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitializeAdmin implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public InitializeAdmin(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.findDistinctByUsername("admin").isPresent()) {
            User admin = new User.UserBuilder()
                    .username("admin")
                    .password("admin")
                    .email("admin")
                    .imageLink("")
                    .setActive(true)
                    .addAuthority(AuthorityType.USER)
                    .addAuthority(AuthorityType.MANAGER)
                    .addAuthority(AuthorityType.ADMIN)
                    .createUser();

            userRepository.save(admin);
        }
    }
}
