package com.mdud.sternumauth.user;


import com.mdud.sternumauth.user.dto.CredentialUserDTO;
import com.mdud.sternumauth.user.dto.UserDTO;
import com.mdud.sternumauth.user.exception.UserAlreadyExistsException;
import com.mdud.sternumauth.user.exception.UserNotFoundException;
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
    public void checkIfUserExists_UserExists_ShouldReturnTrue() {
        assertTrue(userService.checkIfUserExists(mockUser.getUsername()));
    }

    @Test
    public void checkIfUserExists_UserNotExists_ShouldReturnFalse() {
        assertFalse(userService.checkIfUserExists("not existent"));
    }

    @Test
    public void getUserEntity_TestValidScenario() {
        User getUser = userService.getEntityByUsername(mockUser.getUsername());

        verify(userRepository, times(1)).findDistinctByUsername(mockUser.getUsername());
        assertEquals(mockUser, getUser, "should return user entity");
    }

    @Test
    public void getUserEntity_TestInvalidScenarios() {
        assertAll(() -> assertThrows(UserNotFoundException.class,
                () -> userService.getEntityByUsername("notexistent"), "should throw if user not exists")
        );
    }

    @Test
    public void getUser_TestValidScenario() {
        UserDTO getUser = userService.getUserByUsername(mockUser.getUsername());

        verify(userRepository, times(1)).findDistinctByUsername(mockUser.getUsername());
        assertEquals(mockUser.toDTO(), getUser, "should return user");
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

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(credentialUserDTO.toDTO(), newUser, "should add user");
    }

    @Test
    public void addUser_TestInvalidScenario() {
        CredentialUserDTO dtoWithExistingName = new CredentialUserDTO(mockUser.getUsername(), "", "", "");
        CredentialUserDTO dtoWithExistingEmail = new CredentialUserDTO("", "", mockUser.getEmail(), "");

        //i kinda like it
        assertAll(
                () -> assertThrows(UserAlreadyExistsException.class,
                        () -> userService.addUser(dtoWithExistingName),
                        "adding user with same username should throw exception"),

                () -> assertThrows(UserAlreadyExistsException.class,
                        () -> userService.addUser(dtoWithExistingEmail),
                        "adding user with same email should throw exception")
        );
    }

    @Test
    public void changeUserPassword_TestValidScenario() {
        userService.changeUserPassword(mockUser.getUsername(), "newpass");

        verify(userRepository, times(1)).save(mockUser);
        assertTrue(PasswordEncoder.getEncoder().matches("newpass", mockUser.getPassword()), "should change user password to newpassword");
    }

    @Test
    public void changeUserPassword_TestInvalidScenario() {
        assertAll(
                () -> assertThrows(UserNotFoundException.class,
                        () -> userService.changeUserPassword("notexisitng", "newpass"),
                        "should throw exception for not existent user"));
    }

    @Test
    public void changeUserImage_TestValidScenario() {
        userService.changeUserImage(mockUser.getUsername(), "image");

        verify(userRepository, times(1)).save(mockUser);
        assertEquals("image", mockUser.getImageLink(), "should change user image to provided image");
    }

    @Test
    public void changeUserImage_TestInvalidScenarios() {
        assertAll(
                () -> assertThrows(UserNotFoundException.class,
                        () -> userService.changeUserImage("notexistent", ""),
                        "should throw exception for not existent user")
        );
    }

    @Test
    public void activateUser_TestValidScenario() {
        userService.activateUser(mockUser.getUsername());

        verify(userRepository, times(1)).save(mockUser);
        assertTrue(mockUser.getActive(), "should activate user");
    }

    @Test
    public void activateUser_TestInvalidScenarios() {
        assertAll(
                () -> assertThrows(UserNotFoundException.class,
                        () -> userService.activateUser("notexistent"),
                        "should throw exception for not existent user")
        );
    }

    @Test
    public void removeUser_TestValidScenario() {
        assertDoesNotThrow(() -> userService.removeUser(mockUser.getUsername()), "should remove user");
    }

    @Test
    public void removeUser_TestInvalidScenario() {
        assertAll(
                () -> assertThrows(UserNotFoundException.class,
                        () -> userService.removeUser("notexistent"),
                        "removing not existent user should throw exception"));
    }

}