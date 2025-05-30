package com.vivek.salonMicroservice.controller;

import com.vivek.salonMicroservice.mapper.SalonMapper;
import com.vivek.salonMicroservice.model.Salon;
import com.vivek.salonMicroservice.payload.dto.SalonDTO;
import com.vivek.salonMicroservice.payload.dto.UserDTO;
import com.vivek.salonMicroservice.service.SalonService;
import com.vivek.salonMicroservice.service.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/salon")
public class SalonController {

    @Autowired
    private SalonService salonService;

    @Autowired
    private UserFeignClient userService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(
            @RequestBody SalonDTO salon,
            @RequestHeader("Authorization")String jwt
    ) throws Exception {
        UserDTO user=userService.getUserFromJwtToken(jwt).getBody();
        Salon createdSalon = salonService.createSalon(salon,user);
        SalonDTO salonDTO= SalonMapper.mapToDTO(createdSalon);
        return new ResponseEntity<>(salonDTO, HttpStatus.CREATED);
    }


    @PutMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(
            @PathVariable Integer salonId,
            @RequestBody Salon salon,
            @RequestHeader("Authorization")String jwt
    ) throws Exception {

        Salon updatedSalon = salonService.updateSalon(salonId, salon);
        SalonDTO salonDTO=SalonMapper.mapToDTO(updatedSalon);
        return new ResponseEntity<>(salonDTO, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<List<SalonDTO>> getAllSalons() throws Exception {
        List<Salon> salons = salonService.getAllSalons();
        List<SalonDTO> salonDTOS = new ArrayList<>();
        for (Salon salon1 : salons) {
            SalonDTO apply = SalonMapper.mapToDTO(salon1);
            salonDTOS.add(apply);
        }
        return ResponseEntity.ok(salonDTOS);
    }


    @GetMapping("/{salonId}")
    public ResponseEntity<SalonDTO> getSalonById(
            @PathVariable Integer salonId
    ) throws Exception {
        Salon salon = salonService.getSalonById(salonId);
        SalonDTO salonDTO=SalonMapper.mapToDTO(salon);
        return ResponseEntity.ok(salonDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalonDTO>> searchSalon(
            @RequestParam("city") String city
    ) throws Exception {
        List<Salon> salons = salonService.searchSalonByCity(city);
        List<SalonDTO> salonDTOS = new ArrayList<>();
        for (Salon salon1 : salons) {
            SalonDTO apply = SalonMapper.mapToDTO(salon1);
            salonDTOS.add(apply);
        }
        return ResponseEntity.ok(salonDTOS);
    }

    @GetMapping("/owner")
    public ResponseEntity<Salon> getSalonByOwner(
            @RequestHeader("Authorization")String jwt
    ) throws Exception {
        UserDTO user=userService.getUserFromJwtToken(jwt).getBody();
        Salon salon = salonService.getSalonByOwnerId(user.getId());
        return ResponseEntity.ok(salon);
    }
}
