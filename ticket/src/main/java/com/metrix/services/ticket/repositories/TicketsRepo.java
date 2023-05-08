package com.metrix.services.ticket.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.metrix.services.common.domains.Tickets;

public interface TicketsRepo extends JpaRepository<Tickets, Integer> {
 
    @Query("select t from Tickets t JOIN FETCH t.event e where e.id = :eventId")
    List<Tickets> getByEventsId(Integer eventId);

    @Query("select t from Tickets t where t.qrcode = :qrCode")
    Optional<Tickets> getByQrCode(String qrCode);
    
    @Query("select t from Tickets t JOIN FETCH t.event e where e.id = :eventId or t.operator = :operator OR t.qrcode = :qrCode OR t.isClaimed = :isClaimed")
    List<Tickets> query(Integer eventId, String operator, String qrCode, boolean isClaimed);
}