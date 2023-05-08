package com.metrix.services.ticket.receivers;

import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import com.metrix.services.common.domains.MetrixException;
import com.metrix.services.ticket.repositories.TicketsRepo;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import com.metrix.services.common.domains.Tickets;
import com.metrix.services.common.domains.Events;
import com.metrix.services.common.domains.TicketsForm;
import com.metrix.services.ticket.services.EventsServices;
import jakarta.transaction.Transactional;

@Component
public class TicketReceiver {

    @Autowired
    private TicketsRepo repo;   
    
    @Autowired
    private EventsServices eventsSvc;    
    
    /* 
        1. You should implement a method with a specific bean name. The bean name can be 
           find in the application.properties.
           The return type should be a Consumer with generic type TicketsForm and call the 
           createTicket() method.
           You can refer to 
           https://github.com/t217145/COMPS368-Code/blob/main/u9/spring-cloud-stream-demo/src/main/java/com/example/demo/receivers/MyReceiver.java
    */
    @Bean(name = "tma03receiver")
    public Consumer<TicketsForm> input() {
        return this::createTicket;
    }

    @Transactional
    private void createTicket(TicketsForm form){
        try {
            Events events = validate(form);
            for(int i=0; i < form.getNumberOfTicket(); i++){
                Tickets newTicket = new Tickets(0, form.getOperator(), generateQRcode(), false, new Date(), null, events);
                repo.save(newTicket);
            }                
        } catch (MetrixException e) {
            e.printStackTrace();
        }
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

    private Events validate(TicketsForm form) throws MetrixException{
        Optional<Events> oEvent = eventsSvc.retrieve(form.getEventId());

        //Check whether the event is present
        if(!oEvent.isPresent()){
            throw new MetrixException(-1, String.format("TicketReceiver::validate()::Event with Id [%d] not found", form.getEventId()), "");
        } else {
            //Check whether the ticket is active and the corresponding event is start sell
            Events event = oEvent.get();
            if(!event.isStartSell()){
                throw new MetrixException(-1, String.format("TicketReceiver::validate()::Event with Id [%d] not started yet", form.getEventId()), "");
            }

            //Check whether the Venue of the event is full
            //Get the number of seat
            int numOfSeat = event.getVenue().getNumberOfSeat();
            //Get the total number of ticket for that event;
            int ticketSold = repo.getByEventsId(event.getId()).size();
            //Get current ticket needed
            int ticketNeeded = form.getNumberOfTicket();
            if(ticketSold + ticketNeeded > numOfSeat){
                throw new MetrixException(-1, String.format("TicketReceiver::validate()::Event with Id [%d] not started yet", form.getEventId()), "");
            }
            return event;  
        }
    }    
}
