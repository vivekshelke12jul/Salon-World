package com.vivek.salonMicroservice.service;

import com.vivek.salonMicroservice.model.Salon;
import com.vivek.salonMicroservice.payload.dto.SalonDTO;
import com.vivek.salonMicroservice.payload.dto.UserDTO;

import java.util.List;

public interface SalonService {


    Salon createSalon(SalonDTO salon, UserDTO user);

    Salon updateSalon(Integer salonId, Salon salon) throws Exception;

    List<Salon> getAllSalons();

    Salon getSalonById(Integer salonId);

    Salon getSalonByOwnerId(Integer ownerId);

    List<Salon> searchSalonByCity(String city);
}
