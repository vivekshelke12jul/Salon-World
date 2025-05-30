package com.vivek.userMicroservice.service;

import com.vivek.userMicroservice.model.User;
import com.vivek.userMicroservice.payload.dto.KeycloakUserinfo;
import com.vivek.userMicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeycloakService keycloakUserService;

    @Override
    public User saveUser(User user) {
        User newUser = new User(
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getRole()
        );
        return userRepository.save(newUser);
    }

    @Override
    public User findUserbyId(int id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found")
                );
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserbyJwt(String jwt) throws Exception {
        KeycloakUserinfo userInfo = keycloakUserService.fetchUserProfileByJwt(jwt);
        return findUserByEmail(userInfo.getEmail());
    }


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Integer id, User user) {
        User user1 = findUserbyId(id);
        user1.setFullName(user.getFullName());
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setPhone(user.getPhone());
        user1.setRole(user.getRole());
        return userRepository.save(user1);
    }

    @Override
    public void deleteUser(int id) {
        findUserbyId(id);
        userRepository.deleteById(id);
    }
}
