package com.vivek.serviceOfferingMicroservice.service;

import com.vivek.serviceOfferingMicroservice.model.ServiceOffering;
import com.vivek.serviceOfferingMicroservice.payload.dto.CategoryDTO;
import com.vivek.serviceOfferingMicroservice.payload.dto.SalonDTO;
import com.vivek.serviceOfferingMicroservice.payload.dto.ServiceDTO;

import java.util.Set;

public interface ServiceOfferingService {


    ServiceOffering createService(
            ServiceDTO service,
            SalonDTO salon,
            CategoryDTO category
    );

    ServiceOffering updateService(
            Integer serviceId,
            ServiceOffering service
    ) throws Exception;

    Set<ServiceOffering> getAllServicesBySalonId(Integer salonId,Integer categoryId);

    ServiceOffering getServiceById(Integer serviceId) throws Exception;

    Set<ServiceOffering> getServicesByIds(Set<Integer> ids);
}
