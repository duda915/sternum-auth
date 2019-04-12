package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.CredentialUserDTO;
import com.mdud.sternumauth.user.User;
import com.mdud.sternumauth.user.UserDTO;
import com.mdud.sternumauth.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationTokenRepository registrationTokenRepository;
    private final UserService userService;

    @Autowired
    public RegistrationServiceImpl(RegistrationTokenRepository registrationTokenRepository, UserService userService) {
        this.registrationTokenRepository = registrationTokenRepository;
        this.userService = userService;
    }

    @Override
    public RegistrationTokenDTO register(CredentialUserDTO credentialUserDTO) {
        UserDTO persist = userService.addUser(credentialUserDTO);
        User newUser = userService.getEntityByUsername(persist.getUsername());

        RegistrationToken registrationToken = new RegistrationToken(newUser);
        return registrationTokenRepository.save(registrationToken).toDTO();
    }

    @Override
    public UserDTO activateAccount(String token) {
        RegistrationToken registrationToken = registrationTokenRepository.findDistinctByToken(token)
                .orElseThrow(TokenNotFoundException::new);

        UserDTO activatedUser =userService.activateUser(registrationToken.getUser().getUsername());
        registrationTokenRepository.delete(registrationToken);
        return activatedUser;
    }
}
