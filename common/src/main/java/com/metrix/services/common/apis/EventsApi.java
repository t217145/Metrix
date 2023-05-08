package com.metrix.services.common.apis;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.metrix.services.common.domains.Events;
import com.metrix.services.common.domains.EventsForm;
import com.metrix.services.common.domains.MetrixException;
import org.springframework.http.ResponseEntity;

public interface EventsApi {
    @GetMapping({"","/","/index"})
    public List<Events> index() throws MetrixException;

    @GetMapping("/listEventsByVenueId/{venueId}")
    public List<Events> listEventsByVenueId(@PathVariable("venueId") Integer venueId) throws MetrixException;

    @PostMapping("/create")
    public Events create(@RequestBody EventsForm newEvents) throws MetrixException;

    @GetMapping("/retrieve/{eventId}")
    public Optional<Events> retrieve(@PathVariable("eventId") Integer eventId) throws MetrixException;    

    @PostMapping("/update/{eventId}")
    public Events update(@PathVariable("eventId") Integer eventId, @RequestBody EventsForm updatedEvent) throws MetrixException;

    @PostMapping("/changeSellStatus/{eventId}")
    public Events changeSellStatus(@PathVariable("eventId") Integer eventId, @RequestBody boolean isStartSell) throws MetrixException;
    
    @GetMapping("/delete/{eventId}")
    public ResponseEntity<Void> delete(@PathVariable("eventId") Integer eventId) throws MetrixException;
}