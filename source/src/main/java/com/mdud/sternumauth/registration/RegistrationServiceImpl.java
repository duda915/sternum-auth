package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.registration.dto.RegistrationTokenDTO;
import com.mdud.sternumauth.registration.exception.TokenNotFoundException;
import com.mdud.sternumauth.registration.mail.MailService;
import com.mdud.sternumauth.user.dto.CredentialUserDTO;
import com.mdud.sternumauth.user.User;
import com.mdud.sternumauth.user.dto.UserDTO;
import com.mdud.sternumauth.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationTokenRepository registrationTokenRepository;
    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public RegistrationServiceImpl(RegistrationTokenRepository registrationTokenRepository, UserService userService, MailService mailService) {
        this.registrationTokenRepository = registrationTokenRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    private void removeUserIfTokenIsExpired(String username) {
        if (userService.checkIfUserExists(username)) {
            User user = userService.getEntityByUsername(username);
            Optional<RegistrationToken> tokenOptional = registrationTokenRepository.findDistinctByUser(user);

            if (tokenOptional.isPresent()) {
                if (tokenOptional.get().getRegistrationDate().toLocalDate().plusDays(1).isBefore(LocalDate.now())) {
                    registrationTokenRepository.delete(tokenOptional.get());
                    userService.removeUser(username);
                }
            }
        }
    }

    @Override
    public RegistrationTokenDTO register(CredentialUserDTO credentialUserDTO) {
        removeUserIfTokenIsExpired(credentialUserDTO.getUsername());

        UserDTO persist = userService.addUser(credentialUserDTO);
        User newUser = userService.getEntityByUsername(persist.getUsername());

        RegistrationToken registrationToken = new RegistrationToken(newUser);
        registrationToken = registrationTokenRepository.save(registrationToken);

        mailService.sendMail(credentialUserDTO.getEmail(), registrationToken);

        return registrationToken.toDTO();
    }

    @Override
    public UserDTO activateAccount(String token) {
        RegistrationToken registrationToken = registrationTokenRepository.findDistinctByToken(token)
                .orElseThrow(TokenNotFoundException::new);

        UserDTO activatedUser = userService.activateUser(registrationToken.getUser().getUsername());
        registrationTokenRepository.delete(registrationToken);
        return activatedUser;
    }
}
