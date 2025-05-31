package com.vivek.salonMicroservice.service.client;

import com.vivek.salonMicroservice.payload.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("USERMICROSERVICE")
public interface UserFeignClient {

    @GetMapping("/api/users/profile")
    public ResponseEntity<UserDTO> getUserFromJwtToken(
            @RequestHeader("Authorization") String jwt) throws Exception;

    @GetMapping("/api/users/{userId}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable Long userId
    ) throws Exception;
}

