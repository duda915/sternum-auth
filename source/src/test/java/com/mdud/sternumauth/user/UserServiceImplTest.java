package com.mdud.sternumauth.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User mockUser;

    @Before
    public void setup() {
        mockUser = new User.UserBuilder()
                .username("user")
                .password("password")
                .authority(AuthorityType.ADMIN)
                .createUser();
        when(userRepository.findDistinctByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
    }

    @Test
    public void getUser_GetExistentUser_ShouldReturnUser() {
        UserDTO getUser = userService.getUserByUsername(mockUser.getUsername());

        verify(userRepository, times(1)).findDistinctByUsername(mockUser.getUsername());
        assertEquals(mockUser.toDTO(), getUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void getUser_GetNonExistentUser_ShouldThrowUserNotFoundException() {
        userService.getUserByUsername("notexistent");
    }
}