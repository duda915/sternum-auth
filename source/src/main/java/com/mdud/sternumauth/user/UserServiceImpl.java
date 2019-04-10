package com.mdud.sternumauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private User getUser(String username) {
        return userRepository
                .findDistinctByUsername(username).orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return getUser(username).toDTO();
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO changeUserPassword(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO changeUserImage(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO activateUser(UserDTO userDTO) {
        return null;
    }
}
