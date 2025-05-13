package com.vivek.serviceOfferingMicroservice.repository;

import com.vivek.serviceOfferingMicroservice.model.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Integer> {
    Set<ServiceOffering> findBySalonId(Integer id);
}
