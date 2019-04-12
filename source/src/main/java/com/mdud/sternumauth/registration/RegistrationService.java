package com.mdud.sternumauth.registration;

import com.mdud.sternumauth.user.CredentialUserDTO;
import com.mdud.sternumauth.user.UserDTO;

public interface RegistrationService {
    UserDTO register(CredentialUserDTO credentialUserDTO);
    UserDTO activateAccount();
}
