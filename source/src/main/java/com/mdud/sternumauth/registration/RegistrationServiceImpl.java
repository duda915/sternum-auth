package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.CredentialUserDTO;
import com.mdud.sternumauth.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationTokenRepository registrationTokenRepository;

    @Autowired
    public RegistrationServiceImpl(RegistrationTokenRepository registrationTokenRepository) {
        this.registrationTokenRepository = registrationTokenRepository;
    }

    @Override
    public UserDTO register(CredentialUserDTO credentialUserDTO) {
        return null;
    }

    @Override
    public UserDTO activateAccount(String token) {
        return null;
    }
}
