package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.registration.dto.RegistrationTokenDTO;
import com.mdud.sternumauth.registration.exception.TokenNotFoundException;
import com.mdud.sternumauth.registration.mail.MailService;
import com.mdud.sternumauth.user.*;
import com.mdud.sternumauth.user.dto.CredentialUserDTO;
import com.mdud.sternumauth.user.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Date;
import java.time.LocalDate;
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

    @Mock
    private MailService mailService;

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
    public void register_RegisterWithNewUser_ShouldRegisterUser() {
        RegistrationTokenDTO registrationTokenDTO = registrationService.register(credentialTestUser);

        when(userService.checkIfUserExists(testuser.getUsername())).thenReturn(false);

        verify(registrationTokenRepository, never()).delete(any());
        verify(userService, never()).removeUser(credentialTestUser.getUsername());

        verify(userService, times(1)).addUser(credentialTestUser);
        verify(registrationTokenRepository, times(1)).save(any(RegistrationToken.class));
        verify(mailService, times(1)).sendMail(eq("email"), any(RegistrationToken.class));

        assertEquals(credentialTestUser.toDTO(), registrationTokenDTO.getUserDTO(), "registered user should match parameter user");
    }

    @Test
    public void register_RegisterWithExistingUserAndExpiredToken_ShouldRegisterUser() {
        RegistrationToken registrationToken = new RegistrationToken(testuser);
        LocalDate date = registrationToken.getRegistrationDate().toLocalDate().minusDays(1);
        registrationToken.setRegistrationDate(new Date(date.toEpochDay()));

        when(userService.checkIfUserExists(testuser.getUsername())).thenReturn(true);

        when(registrationTokenRepository.findDistinctByUser(testuser)).thenReturn(Optional.of(registrationToken));

        assertDoesNotThrow(() -> registrationService.register(credentialTestUser));

        verify(userService, times(1)).removeUser(credentialTestUser.getUsername());
        verify(registrationTokenRepository, times(1)).delete(registrationToken);
        verify(userService, times(1)).addUser(credentialTestUser);
        verify(registrationTokenRepository, times(1)).save(any(RegistrationToken.class));
        verify(mailService, times(1)).sendMail(eq("email"), any(RegistrationToken.class));
    }

    @Test
    public void register_RegisterWithExistingUserAndNotExpiredToken_ShouldNotRemoveUser() {
        RegistrationToken registrationToken = new RegistrationToken(testuser);

        when(userService.checkIfUserExists(testuser.getUsername())).thenReturn(true);
        when(registrationTokenRepository.findDistinctByUser(testuser)).thenReturn(Optional.of(registrationToken));

        assertDoesNotThrow(() -> registrationService.register(credentialTestUser));

        verify(userService, never()).removeUser(credentialTestUser.getUsername());
        verify(registrationTokenRepository, never()).delete(registrationToken);
        verify(userService, times(1)).addUser(credentialTestUser);
    }

    @Test
    public void register_RegisterWithExistingUserAndNotExistentToken_ShouldNotRemoveUser() {
        when(userService.checkIfUserExists(testuser.getUsername())).thenReturn(true);

        assertDoesNotThrow(() -> registrationService.register(credentialTestUser));
        verify(userService, never()).removeUser(credentialTestUser.getUsername());
        verify(registrationTokenRepository, never()).delete(any());
        verify(userService, times(1)).addUser(credentialTestUser);
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