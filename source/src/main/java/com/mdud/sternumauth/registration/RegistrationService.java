package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.registration.dto.RegistrationTokenDTO;
import com.mdud.sternumauth.user.dto.CredentialUserDTO;
import com.mdud.sternumauth.user.dto.UserDTO;

public interface RegistrationService {
    RegistrationTokenDTO register(CredentialUserDTO credentialUserDTO);
    UserDTO activateAccount(String token);
}
