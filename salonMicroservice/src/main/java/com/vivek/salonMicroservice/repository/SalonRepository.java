package com.vivek.salonMicroservice.repository;

import com.vivek.salonMicroservice.model.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Integer> {
    Salon findByOwnerId(Integer ownerId);

    @Query("""
            SELECT s FROM Salon s WHERE 
            s.active = true AND
            (
            LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
            LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR 
            LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    List<Salon> findSalons(@Param("keyword") String keyword);
}
