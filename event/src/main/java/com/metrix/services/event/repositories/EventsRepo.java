package com.metrix.services.event.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.metrix.services.common.domains.Events;

public interface EventsRepo extends JpaRepository<Events, Integer> {

}