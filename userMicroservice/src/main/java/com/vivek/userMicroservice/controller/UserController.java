package com.vivek.userMicroservice.controller;

import com.vivek.userMicroservice.model.User;
import com.vivek.userMicroservice.payload.dto.UserDto;
import com.vivek.userMicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findUserbyId(id));
    }
    
    @GetMapping("/api/users/profile")
    public ResponseEntity<UserDto> getUserFromJwtToken(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserbyJwt(jwt);
        UserDto UserDto= new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().toString());

        return new ResponseEntity<>(UserDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

}
