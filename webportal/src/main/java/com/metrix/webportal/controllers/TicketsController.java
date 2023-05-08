package com.metrix.webportal.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.metrix.webportal.domains.SellForm;
import com.metrix.webportal.services.TicketsServices;
import com.metrix.webportal.services.EventsServices;
import com.metrix.services.common.domains.MetrixException;
import jakarta.validation.Valid;
import com.metrix.services.common.domains.Tickets;
import com.metrix.services.common.domains.TicketsForm;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
    private static final String VIEW_PREFIX = "tickets/";
    private static final String HARD_CODE_OPERATOR = "Dummy Operator";

    @Autowired
    private TicketsServices ticketSvc;

    @Autowired
    private EventsServices eventSvc;    

    /* 1. You need to dependency inject something so that you can send message 
     * to the spring cloud stream binding. Please refer to 
     * https://github.com/t217145/COMPS368-Code/blob/main/u9/spring-cloud-stream-demo/src/main/java/com/example/demo/controllers/TestController.java
    */
    @Autowired
	private StreamBridge bridge;    

    @GetMapping("/list/{eventId}")
    public String list(ModelMap m, @PathVariable("eventId") Integer eventId) throws MetrixException{
        List<Tickets> allTickets = ticketSvc.listTicketsByEventId(eventId);
        m.addAttribute("allTickets", allTickets);
        m.addAttribute("ticketDetail", new Tickets());
        return VIEW_PREFIX + "list"; 
    }

    @GetMapping("/create")
    public String create(ModelMap m) throws MetrixException{
        m.addAttribute("newSellForm", new SellForm());
        m.addAttribute("allEvents", eventSvc.index());
        return VIEW_PREFIX + "create";
    }

    @PostMapping("/create")
    @Transactional
    public String create(ModelMap m, @Valid @ModelAttribute("newSellForm") TicketsForm newSellForm, BindingResult result) throws MetrixException{
        if(result.hasErrors()){
            m.addAttribute("newSellForm", newSellForm);
            m.addAttribute("allEvents", eventSvc.index());
            return VIEW_PREFIX + "create";
        }
        newSellForm.setOperator(HARD_CODE_OPERATOR);

        /* 2. Call the bridge to send out the newSellForm with binding.
        *     You need to look up in the application.properties for the binding name.
        *     Beware, different from receiver, the send out binding here need to include
        *     the suffix as well
        *     You can refer to https://github.com/t217145/COMPS368-Code/blob/main/u9/spring-cloud-stream-demo/src/main/java/com/example/demo/controllers/TestController.java
        */
        boolean isSuccess = bridge.send("rmqtma04-out-0", newSellForm);

        if(!isSuccess){
            throw new MetrixException(900 ,"Send message to ticket service failed", VIEW_PREFIX + "create");
        }

        return "redirect:/" + (VIEW_PREFIX + "list/" + newSellForm.getEventId());
    }

    //Query ticket info
    @GetMapping({"","/","/query"})
    public String query(ModelMap m){
        m.addAttribute("ticketDetail", new Tickets());
        return VIEW_PREFIX + "query";
    }

    @PostMapping("/detail")
    public String detail(ModelMap m, @ModelAttribute("ticketDetail") Tickets queryTicket) throws MetrixException{
        if(queryTicket.getQrcode().trim().isEmpty()){
            throw new MetrixException(-1, "Ticket QR code must be provided!", "/" + VIEW_PREFIX + "query");
        }

        Optional<Tickets> oTicket = ticketSvc.retrieveByQrCode(queryTicket.getQrcode().trim());

        if(!oTicket.isPresent()){
            throw new MetrixException(-2, "Ticket not found!", "/" + VIEW_PREFIX + "query");
        }

        m.addAttribute("ticketDetail", oTicket.get());
        return VIEW_PREFIX + "detail"; 
    }

    @PostMapping("/claim")
    public String claim(ModelMap m, @ModelAttribute("ticketDetail") Tickets claimTicket) throws MetrixException{
        if(claimTicket.getQrcode().trim().isEmpty()){
            throw new MetrixException(-1, "Ticket QR code must be provided!", "/" + VIEW_PREFIX + "query");
        }

        Tickets claimedTicket = ticketSvc.claim(claimTicket.getQrcode());

        //show the ticket info in view /tickets/detail.html
        m.addAttribute("ticketDetail", claimedTicket);
        return VIEW_PREFIX + "detail";     
    }

    @GetMapping("/delete/{qrcode}")
    public String delete(@PathVariable("qrcode") String qrcode) throws MetrixException{
        Optional<Tickets> oTicket = ticketSvc.retrieveByQrCode(qrcode);
        ticketSvc.delete(qrcode);
        return "redirect:/" + VIEW_PREFIX + "list/" + oTicket.get().getEvent().getId();
    }  
}
