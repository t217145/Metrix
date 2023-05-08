package com.metrix.services.common.apis;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.metrix.services.common.domains.MetrixException;
import com.metrix.services.common.domains.Tickets;
import com.metrix.services.common.domains.TicketsForm;

public interface TicketApi {
    @GetMapping({"","/","/index"})
    public List<Tickets> index() throws MetrixException;

    @GetMapping("/listTicketsByEventId/{eventId}")
    public List<Tickets> listTicketsByEventId(@PathVariable("eventId") Integer eventId) throws MetrixException;

    @PostMapping("/claim/{qrcode}")
    public Tickets claim(@PathVariable("qrcode") String qrcode) throws MetrixException;    

    @PostMapping("/create")
    public List<Tickets> create(@RequestBody TicketsForm newTicketForm) throws MetrixException;

    @GetMapping("/retrieve/{ticketId}")
    public Optional<Tickets> retrieve(@PathVariable("ticketId") Integer ticketId) throws MetrixException;

    @GetMapping("/query/{qrCode}")
    public Optional<Tickets> retrieveByQrCode(@PathVariable("qrCode") String qrCode) throws MetrixException;  

    @PostMapping("/update/{ticketId}")
    public Tickets update(@PathVariable("ticketId") Integer ticketId, @RequestBody Tickets updatedTicket) throws MetrixException;        

    @GetMapping("/delete/{qrCode}")
    public ResponseEntity<Void> delete(@PathVariable("qrCode") String qrCode) throws MetrixException;
}