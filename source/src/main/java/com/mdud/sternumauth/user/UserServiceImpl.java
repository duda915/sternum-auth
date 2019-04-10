package com.mdud.sternumauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return userRepository.findDistinctByUsername(username).orElseThrow(UserNotFoundException::new).toDTO();
    }

    private boolean checkIfUsernameAlreadyExists(String username) {
        return userRepository.findDistinctByUsername(username).isPresent();
    }

    private boolean checkIfEmailAlreadyExists(String email) {
        return userRepository.findDistinctByEmail(email).isPresent();
    }

    @Override
    public UserDTO addUser(CredentialUserDTO credentialUserDTO) {
        if(checkIfEmailAlreadyExists(credentialUserDTO.getEmail())) {
            throw new UserAlreadyExistsException(UserAlreadyExistsException.Type.Email);
        } else if (checkIfUsernameAlreadyExists(credentialUserDTO.getUsername())) {
            throw new UserAlreadyExistsException(UserAlreadyExistsException.Type.Username);
        }

        return userRepository.save(User.fromCredentialUserDTO(credentialUserDTO)).toDTO();
    }

    @Override
    public UserDTO changeUserPassword(String username, String newPassword) {
        User user = userRepository.findDistinctByUsername(username).orElseThrow(UserNotFoundException::new);
        user.setPassword(newPassword);

        return userRepository.save(user).toDTO();
    }

    @Override
    public UserDTO changeUserImage(String username, String image) {
        User user = userRepository.findDistinctByUsername(username).orElseThrow(UserNotFoundException::new);
        user.setImageLink(image);

        return userRepository.save(user).toDTO();
    }

    @Override
    public UserDTO activateUser(String username) {
        User user = userRepository.findDistinctByUsername(username).orElseThrow(UserNotFoundException::new);
        user.setActive(true);

        return userRepository.save(user).toDTO();
    }

}
