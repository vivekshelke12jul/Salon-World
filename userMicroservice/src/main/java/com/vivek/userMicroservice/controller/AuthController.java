package com.vivek.userMicroservice.controller;

import com.vivek.userMicroservice.payload.dto.LoginDto;
import com.vivek.userMicroservice.payload.dto.SignupDto;
import com.vivek.userMicroservice.payload.response.ApiResponse;
import com.vivek.userMicroservice.payload.response.AuthResponse;
import com.vivek.userMicroservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse> welcome() {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse("welcome to auth api"));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(@RequestBody SignupDto req) throws Exception {

        System.out.println("signup dto "+req);
        AuthResponse response=authService.signup(req);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginDto req) throws Exception {

        AuthResponse response=authService.login(req.getEmail(), req.getPassword());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/access-token/refresh-token/{refreshToken}")
    public ResponseEntity<AuthResponse> getAccessTokenHandler(@PathVariable String refreshToken) throws Exception {

        AuthResponse response = authService.getAccessTokenFromRefreshToken(refreshToken);

        return ResponseEntity.ok(response);
    }

}

