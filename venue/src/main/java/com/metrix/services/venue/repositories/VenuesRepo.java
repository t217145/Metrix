package com.metrix.services.venue.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.metrix.services.common.domains.Venues;

public interface VenuesRepo extends JpaRepository<Venues, Integer> {
    
}