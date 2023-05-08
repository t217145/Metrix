package com.metrix.services.ticket.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.metrix.services.common.apis.TicketApi;
import com.metrix.services.common.domains.MetrixException;
import com.metrix.services.common.domains.Tickets;
import com.metrix.services.common.domains.TicketsForm;
import com.metrix.services.ticket.repositories.TicketsRepo;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping(value = "/api/tickets")
public class TicketsApiController implements TicketApi{

    @Autowired
    private TicketsRepo repo;

    @Override
    public List<Tickets> index() throws MetrixException {
        return repo.findAll();
    }

    @Override
    public List<Tickets> listTicketsByEventId(Integer eventId) throws MetrixException {
        return repo.getByEventsId(eventId);
    }

    @Override
    public Tickets claim(String qrcode) throws MetrixException {
        Optional<Tickets> oTickets = repo.getByQrCode(qrcode);
        if(!oTickets.isPresent()){
            throw new MetrixException(-1, String.format("TicketsApiController::claim()::Ticket with QR Code [%s] not found", qrcode), "");
        }
        Tickets ticket = oTickets.get();
        ticket.setClaimDtm(new Date());
        ticket.setClaimed(true);
        return repo.save(ticket);
    }

    @Override
    @Transactional
    public List<Tickets> create(TicketsForm newTicketForm) throws MetrixException { 
        throw new MetrixException(999, "Ticket create Api is depreciated!", "");
    }

    @Override
    public Optional<Tickets> retrieve(Integer ticketId) throws MetrixException {
        return repo.findById(ticketId);
    }

    @Override
    public Optional<Tickets> retrieveByQrCode(String qrCode) throws MetrixException {
        return repo.getByQrCode(qrCode);
    }    

    @Override
    @Transactional
    public Tickets update(Integer ticketId, Tickets updatedTicket) throws MetrixException {
        Optional<Tickets> oDbTickets = repo.findById(ticketId);
        if(oDbTickets.isEmpty()){
            throw new MetrixException(-1, String.format("TicketsApiController::update()::Ticket with Id [%d] not found", ticketId), "");
        }
        Tickets dbTickets = oDbTickets.get();
        if(dbTickets.isClaimed()){
            throw new MetrixException(-1, String.format("TicketsApiController::update()::Ticket with Id [%d] already claimed and cannot be change", ticketId), "");
        }
        dbTickets.setOperator(updatedTicket.getOperator());
        dbTickets.setQrcode(generateQRcode()); //Void the old ticket and force reprint
        return repo.save(dbTickets);
    }

    @Override
    public ResponseEntity<Void> delete(String qrCode) throws MetrixException {
        Optional<Tickets> oDbTickets = repo.getByQrCode(qrCode);
        if(!oDbTickets.isPresent()){
            throw new MetrixException(-1, String.format("TicketsApiController::delete()::Ticket with Qr Code [%s] not found", qrCode), "");
        }

        Tickets ticket = oDbTickets.get();
        if(ticket.isClaimed()){
            throw new MetrixException(-1, String.format("TicketsApiController::delete()::Ticket with Qr Code [%s] already claimed", qrCode), "");
        }
        repo.deleteById(ticket.getId());
        return ResponseEntity.noContent().build();
    }

    private String generateQRcode(){
        //generate qr code but do while loop, you can assume it will never endless. Safe guard by only have max 100 seat
        boolean isDuplicated = true;
        String qrCode = "";
        do{
            qrCode = UUID.randomUUID().toString();
            if(!repo.getByQrCode(qrCode).isPresent()){
                isDuplicated = false;
            }
        }while(isDuplicated);
        return qrCode;
    }
}