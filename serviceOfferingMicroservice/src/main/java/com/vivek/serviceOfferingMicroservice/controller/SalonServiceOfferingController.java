package com.vivek.serviceOfferingMicroservice.controller;

import com.vivek.serviceOfferingMicroservice.model.ServiceOffering;
import com.vivek.serviceOfferingMicroservice.payload.dto.CategoryDTO;
import com.vivek.serviceOfferingMicroservice.payload.dto.SalonDTO;
import com.vivek.serviceOfferingMicroservice.payload.dto.ServiceDTO;
import com.vivek.serviceOfferingMicroservice.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-offering/salon-owner")
public class SalonServiceOfferingController {

    @Autowired
    private ServiceOfferingService serviceOfferingService;

    @PostMapping
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO service,
            @RequestBody SalonDTO salon,
            @RequestBody CategoryDTO category
    ) throws Exception {
        ServiceOffering createdService = serviceOfferingService.createService(service,salon,category);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PatchMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> updateService(
            @PathVariable Integer serviceId,
            @RequestBody ServiceOffering service
    ) throws Exception {
        ServiceOffering updatedService = serviceOfferingService.updateService(serviceId, service);
        return new ResponseEntity<>(updatedService, HttpStatus.OK);
    }

}

