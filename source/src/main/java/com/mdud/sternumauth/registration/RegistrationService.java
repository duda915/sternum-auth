package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.CredentialUserDTO;
import com.mdud.sternumauth.user.UserDTO;

public interface RegistrationService {
    RegistrationTokenDTO register(CredentialUserDTO credentialUserDTO);
    UserDTO activateAccount(String token);
}
