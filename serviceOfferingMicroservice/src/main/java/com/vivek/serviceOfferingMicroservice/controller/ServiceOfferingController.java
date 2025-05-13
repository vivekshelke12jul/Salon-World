package com.vivek.serviceOfferingMicroservice.controller;

import com.vivek.serviceOfferingMicroservice.model.ServiceOffering;
import com.vivek.serviceOfferingMicroservice.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/service-offering")
public class ServiceOfferingController {

    @Autowired
    private ServiceOfferingService serviceOfferingService;


    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(
            @PathVariable Integer salonId,
            @RequestParam(required = false) Integer categoryId
    ) {
        Set<ServiceOffering> services =  serviceOfferingService.getAllServicesBySalonId(salonId,categoryId);
        return ResponseEntity.ok(services);
    }


    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceOffering> getServiceById(@PathVariable Integer serviceId) throws Exception {
        ServiceOffering service = serviceOfferingService.getServiceById(serviceId);
        return ResponseEntity.ok(service);
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOffering>> getServicesByIds(@PathVariable Set<Integer> ids) {
        Set<ServiceOffering> services =  serviceOfferingService.getServicesByIds(ids);
        return ResponseEntity.ok(services);
    }

}

