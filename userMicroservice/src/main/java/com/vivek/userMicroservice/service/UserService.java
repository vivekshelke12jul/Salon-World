package com.vivek.userMicroservice.service;

import com.vivek.userMicroservice.model.User;

import java.util.List;

public interface UserService {
    public User saveUser(User user);
    public User findUserbyId(int id);
    public List<User> findAllUsers();
    public User updateUser(Integer id, User user);
    public void deleteUser(int id);
}
