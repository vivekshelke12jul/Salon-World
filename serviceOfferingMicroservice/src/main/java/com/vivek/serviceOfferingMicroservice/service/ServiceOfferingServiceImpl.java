package com.vivek.serviceOfferingMicroservice.service;

import com.vivek.serviceOfferingMicroservice.model.ServiceOffering;
import com.vivek.serviceOfferingMicroservice.payload.dto.CategoryDTO;
import com.vivek.serviceOfferingMicroservice.payload.dto.SalonDTO;
import com.vivek.serviceOfferingMicroservice.payload.dto.ServiceDTO;
import com.vivek.serviceOfferingMicroservice.repository.ServiceOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    @Autowired
    private ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(ServiceDTO service, SalonDTO salon, CategoryDTO category) {

        ServiceOffering serviceOffering=new ServiceOffering();
        serviceOffering.setName(service.getName());
        serviceOffering.setDescription(service.getDescription());
        serviceOffering.setPrice(service.getPrice());
        serviceOffering.setDuration(service.getDuration());
        serviceOffering.setImage(service.getImage());
        serviceOffering.setSalonId(salon.getId());
        serviceOffering.setCategoryId(category.getId());
        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Integer serviceId, ServiceOffering service) throws Exception {
        ServiceOffering serviceOffering=getServiceById(serviceId);
        serviceOffering.setName(service.getName());
        serviceOffering.setDescription(service.getDescription());
        serviceOffering.setPrice(service.getPrice());
        serviceOffering.setDuration(service.getDuration());
        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public Set<ServiceOffering> getAllServicesBySalonId(Integer salonId, Integer categoryId) {
        Set<ServiceOffering> services = serviceOfferingRepository.findBySalonId(salonId);
        if(categoryId != null) {
            services=services.stream()
                    .filter(service -> service.getCategoryId() != null && service.getCategoryId().equals(categoryId))
                    .collect(Collectors.toSet());
        }
        return services;
    }

    @Override
    public ServiceOffering getServiceById(Integer serviceId) throws Exception {
        return serviceOfferingRepository.findById(serviceId)
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.NOT_FOUND ,"Service not found with id: "+serviceId)
                );
    }

    @Override
    public Set<ServiceOffering> getServicesByIds(Set<Integer> ids) {
        List<ServiceOffering> services=serviceOfferingRepository.findAllById(ids);
        return new HashSet<>(services);
    }
}
