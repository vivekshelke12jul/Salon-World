package com.vivek.reviewMicroservice.service.client;

import com.vivek.reviewMicroservice.payload.dto.SalonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("SALON")
public interface SalonFeignClient {

    @GetMapping("/api/salons/owner")
    public ResponseEntity<SalonDTO> getSalonByOwner(
            @RequestHeader("Authorization")String jwt) throws Exception;

    @GetMapping("/api/salons/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Integer salonId) throws Exception;
}

