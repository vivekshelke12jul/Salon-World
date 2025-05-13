package com.vivek.salonMicroservice.mapper;

import com.vivek.salonMicroservice.model.Salon;
import com.vivek.salonMicroservice.payload.dto.SalonDTO;
import com.vivek.salonMicroservice.payload.dto.UserDTO;

public class SalonMapper {

    public static SalonDTO mapToDTO(Salon salon) {
        if (salon == null) {
            return null;
        }

        SalonDTO salonDTO = new SalonDTO();

        salonDTO.setId(salon.getId());
        salonDTO.setName(salon.getName());
        salonDTO.setAddress(salon.getAddress());
        salonDTO.setPhoneNumber(salon.getPhoneNumber());
        salonDTO.setEmail(salon.getEmail());
        salonDTO.setCity(salon.getCity());

//        salonDTO.setHomeService(salon.isHomeService());

        salonDTO.setOwnerId(salon.getOwnerId());
        salonDTO.setOpen(salon.isOpen());
        salonDTO.setActive(salon.isActive());
        salonDTO.setOpenTime(salon.getOpenTime());
        salonDTO.setCloseTime(salon.getCloseTime());
        salonDTO.setImages(salon.getImages());

        return salonDTO;
    }

}
