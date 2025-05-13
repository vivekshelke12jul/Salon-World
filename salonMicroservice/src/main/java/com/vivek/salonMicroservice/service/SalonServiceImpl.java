package com.vivek.salonMicroservice.service;

import com.vivek.salonMicroservice.model.Salon;
import com.vivek.salonMicroservice.payload.dto.SalonDTO;
import com.vivek.salonMicroservice.payload.dto.UserDTO;
import com.vivek.salonMicroservice.repository.SalonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SalonServiceImpl implements SalonService {

    @Autowired
    private SalonRepository salonRepository;

    @Override
    public Salon createSalon(SalonDTO salon, UserDTO user) {
        Salon newSalon = new Salon(
                salon.getName(),
                salon.getEmail(),
                salon.getPhoneNumber(),
                salon.getAddress(),
                salon.getCity(),

                true,
                true,
                user.getId(),
                salon.getOpenTime(),
                salon.getCloseTime(),
                salon.getImages()
        );
        return salonRepository.save(newSalon);
    }

    @Override
    public Salon updateSalon(Integer salonId, Salon salon) throws Exception {
        Salon salon1 = getSalonById(salonId);

        salon1.setName(salon.getName());
        salon1.setEmail(salon.getEmail());
        salon1.setPhoneNumber(salon.getPhoneNumber());
        salon1.setAddress(salon.getAddress());
        salon1.setCity(salon.getCity());

        salon1.setActive(salon.isActive());
        salon1.setOpen(salon.isOpen());
        salon1.setOpenTime(salon.getOpenTime());
        salon1.setCloseTime(salon.getCloseTime());
        salon1.setImages(salon.getImages());

        return salonRepository.save(salon1);
    }

    @Override
    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Integer salonId) {
        return salonRepository.findById(salonId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Salon not found with id : " + salonId)
                );
    }

    @Override
    public Salon getSalonByOwnerId(Integer ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCity(String city) {
        return salonRepository.findSalons(city);
    }
}
