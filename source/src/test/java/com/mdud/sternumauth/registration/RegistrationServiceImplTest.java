package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.*;
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
public class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private RegistrationTokenRepository registrationTokenRepository;

    @Mock
    private UserService userService;

    private User testuser;
    private CredentialUserDTO credentialTestUser;

    @BeforeEach
    public void setup() {
        testuser = new User.UserBuilder()
                .username("user")
                .password("pass")
                .email("email")
                .imageLink("")
                .addAuthority(AuthorityType.USER)
                .createUser();
        credentialTestUser = new CredentialUserDTO(testuser.getUsername(), testuser.getPassword(), testuser.getEmail(), testuser.getImageLink());
        when(userService.addUser(credentialTestUser)).thenReturn(testuser.toDTO());
        when(userService.getEntityByUsername(credentialTestUser.getUsername())).thenReturn(testuser);
        when(registrationTokenRepository.save(any(RegistrationToken.class))).then(it -> it.getArgument(0));
    }

    @Test
    public void register_TestValidScenario() {
        RegistrationTokenDTO registrationTokenDTO = registrationService.register(credentialTestUser);

        verify(userService, times(1)).addUser(credentialTestUser);
        verify(registrationTokenRepository).save(any(RegistrationToken.class));

        assertEquals(credentialTestUser.toDTO(), registrationTokenDTO.getUserDTO(), "registered user should match parameter user");
    }

    @Test
    public void activateAccount_TestValidScenario() {
        RegistrationToken registrationToken = new RegistrationToken(testuser);

        when(registrationTokenRepository.findDistinctByToken(registrationToken.getToken()))
                .thenReturn(Optional.of(registrationToken));
        when(userService.activateUser(testuser.getUsername())).thenReturn(testuser.toDTO());

        UserDTO userDTO = registrationService.activateAccount(registrationToken.getToken());

        verify(registrationTokenRepository, times(1)).delete(registrationToken);
        verify(userService, times(1)).activateUser(testuser.getUsername());
        assertEquals(testuser.toDTO(), userDTO);
    }

    @Test
    public void activateAccount_TestInvalidScenario() {
        assertAll(
                () -> assertThrows(TokenNotFoundException.class,
                        () -> registrationService.activateAccount("not"),
                        "should throw exception if token not exists")
        );
    }
}