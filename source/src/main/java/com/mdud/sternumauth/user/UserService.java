package com.mdud.sternumauth.user;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByUsername(String username);
    User addUser(UserDTO userDTO);
    User changeUserPassword(UserDTO userDTO);
    User changeUserImage(UserDTO userDTO);
    User activateUser(UserDTO userDTO);
}
