package com.mdud.sternumauth.user;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    public void setup() {
        mockUser = new User.UserBuilder()
                .username("user")
                .password("password")
                .email("email")
                .addAuthority(AuthorityType.ADMIN)
                .addAuthority(AuthorityType.MANAGER)
                .addAuthority(AuthorityType.USER)
                .createUser();

        when(userRepository.findDistinctByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
        when(userRepository.findDistinctByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any())).then(it -> it.getArgument(0));
    }

    @Test
    public void getUser_TestValidScenario() {
        UserDTO getUser = userService.getUserByUsername(mockUser.getUsername());

        verify(userRepository, times(1)).findDistinctByUsername(mockUser.getUsername());
        assertEquals(mockUser.toDTO(), getUser);
    }

    @Test
    public void getUser_TestInvalidScenarios() {
        assertAll(() -> assertThrows(UserNotFoundException.class,
                () -> userService.getUserByUsername("notexistent"), "should throw if user not exists")
        );
    }

    @Test
    public void addUser_TestValidScenario() {
        CredentialUserDTO credentialUserDTO = new CredentialUserDTO("new", "plain", "newmail", "image");

        UserDTO newUser = userService.addUser(credentialUserDTO);

        verify(userRepository, times(1)).findDistinctByUsername(credentialUserDTO.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(credentialUserDTO.toDTO(), newUser);
    }

    @Test
    public void addUser_TestInvalidScenario() {
        CredentialUserDTO dtoWithExistingName = new CredentialUserDTO("user", "", "", "");
        CredentialUserDTO dtoWithExistingEmail = new CredentialUserDTO("", "", "email", "");

        //i kinda like it
        assertAll(() -> assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(dtoWithExistingName), "adding user with same username should not be possible"),
                () -> assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(dtoWithExistingEmail), "adding user with same email should not be possibe"));
    }

    @Test
    public void changeUserPassword_ChangeToNewPassword_ShouldChangePassword() {

    }

}