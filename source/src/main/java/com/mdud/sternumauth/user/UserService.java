package com.mdud.sternumauth.user;

import java.util.Optional;

public interface UserService {
    boolean checkIfUserExists(String username);
    User getEntityByUsername(String username);
    UserDTO getUserByUsername(String username);
    UserDTO addUser(CredentialUserDTO credentialUserDTO);
    UserDTO changeUserPassword(String username, String newPassword);
    UserDTO changeUserImage(String username, String image);
    UserDTO activateUser(String username);
}
