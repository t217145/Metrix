package com.metrix.services.venue.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.metrix.services.common.apis.VenueApi;
import com.metrix.services.common.domains.MetrixException;
import com.metrix.services.common.domains.Venues;
import com.metrix.services.venue.repositories.VenuesRepo;
import com.metrix.services.venue.services.EventsServices;

@RestController
@RequestMapping(value = "/api/venues")
public class VenuesApiController implements VenueApi{

    @Autowired
    private VenuesRepo repo;

    @Autowired
    private EventsServices eventsSvc;

    @Override
    public List<Venues> index() throws MetrixException {
        return repo.findAll();
    }

    @Override
    public Venues create(Venues newVenue) throws MetrixException {
        return repo.save(newVenue);
    }

    @Override
    public Optional<Venues> retrieve(Integer venueId) throws MetrixException {
        return repo.findById(venueId);
    }

    @Override
    public Venues update(Integer venueId, Venues updatedVenue) throws MetrixException {
        Optional<Venues> oDbVenues = repo.findById(venueId);
        if(!oDbVenues.isPresent()){
            throw new MetrixException(-1, String.format("VenuesApiController::update()::Venue with Id [%d] not found", venueId), "");
        }
        Venues dbVenues = oDbVenues.get();
        if(updatedVenue.getNumberOfSeat() < dbVenues.getNumberOfSeat() && !eventsSvc.listEventsByVenueId(venueId).isEmpty()){
            throw new MetrixException(-1, "Venue already linked with event(s), reduce seat number is not allowed", "");
        }
        dbVenues.setNumberOfSeat(updatedVenue.getNumberOfSeat());
        dbVenues.setVenueAddr(updatedVenue.getVenueAddr());
        dbVenues.setVenueName(updatedVenue.getVenueName());
        return repo.save(dbVenues);
    }

    @Override
    public ResponseEntity<Void> delete(Integer venueId) throws MetrixException {
        Optional<Venues> oDbVenues = repo.findById(venueId);
        if(!oDbVenues.isPresent()){
            throw new MetrixException(-1, String.format("VenuesApiController::update()::Venue with Id [%d] not found", venueId), "");
        }
        if(!eventsSvc.listEventsByVenueId(venueId).isEmpty()){
            throw new MetrixException(-1, "Venue already linked with event(s), reduce seat number is not allowed", "");
        }        
        repo.deleteById(venueId);
        return ResponseEntity.noContent().build();
    }
    
}
