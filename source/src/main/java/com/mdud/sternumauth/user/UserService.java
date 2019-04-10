package com.mdud.sternumauth.user;

import java.util.Optional;

public interface UserService {
    UserDTO getUserByUsername(String username);
    UserDTO addUser(UserDTO userDTO);
    UserDTO changeUserPassword(UserDTO userDTO);
    UserDTO changeUserImage(UserDTO userDTO);
    UserDTO activateUser(UserDTO userDTO);
}
