package com.metrix.webportal.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.metrix.services.common.domains.Events;
import com.metrix.services.common.domains.EventsForm;
import com.metrix.services.common.domains.MetrixException;
import com.metrix.webportal.services.EventsServices;
import com.metrix.webportal.services.TicketsServices;
import com.metrix.webportal.services.VenuesServices;
import jakarta.validation.Valid;

@Controller
@RequestMapping({"/events","/",""})
public class EventsController {
    private static final String VIEW_PREFIX = "events/";

    @Autowired
    private EventsServices eventSvc;

    @Autowired
    private TicketsServices ticketSvc;    

    @Autowired
    private VenuesServices venueSvc;    

    @GetMapping({"","/","/index"})
    public String index(ModelMap m) throws MetrixException{
        List<Events> allEvents = eventSvc.index();
        allEvents.stream().forEach(e -> {
            try {
                e.setTickets(ticketSvc.listTicketsByEventId(e.getId()));
            } catch (MetrixException e1) {
                e1.printStackTrace();
            }
        });
        m.addAttribute("allEvents", allEvents);
        return VIEW_PREFIX + "index";
    }

    @GetMapping("/create")
    public String create(ModelMap m) throws MetrixException{
        m.addAttribute("newEvent", new EventsForm());
        m.addAttribute("allVenues", venueSvc.index());
        return VIEW_PREFIX + "create";
    }

    @PostMapping("/create")
    public String create(ModelMap m, @Valid @ModelAttribute("newEvent") EventsForm newEvent, BindingResult result) throws MetrixException{
        if(result.hasErrors()){
            m.addAttribute("newEvent", newEvent);
            m.addAttribute("allVenues", venueSvc.index());
            return VIEW_PREFIX + "create";
        }
        newEvent.setStartSell(false);
        eventSvc.create(newEvent);
        return "redirect:/" + VIEW_PREFIX + "index";
    }

    @GetMapping("/update/{id}")
    public String update(ModelMap m, @PathVariable("id")Integer id) throws MetrixException{
        Optional<Events> oEvent = eventSvc.retrieve(id);
        //if event with id not found, throw exception, include redirect url to /events/index in error page
        if(!oEvent.isPresent()){
            throw new MetrixException(-1, String.format("Event with id {%d} not found!", id), "/" + VIEW_PREFIX + "index");
        }
        Events events = oEvent.get();
        EventsForm form = new EventsForm(events.getId(), events.getEventName(), events.isStartSell(), events.getEventDtm(), events.getVenue().getId());
        m.addAttribute("updEvent", form);
        m.addAttribute("allVenues", venueSvc.index());
        return VIEW_PREFIX + "update";
    }

    @PostMapping("/update/{id}")
    public String update(ModelMap m, @PathVariable("id")Integer id, @Valid @ModelAttribute("updEvent") EventsForm updEvent, BindingResult result) throws MetrixException{
        if(result.hasErrors()){
            m.addAttribute("updVenue", updEvent);
            m.addAttribute("allVenues", venueSvc.index());
            return VIEW_PREFIX + "update";
        }
        eventSvc.update(id, updEvent);
        return "redirect:/" + VIEW_PREFIX + "index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Integer id) throws MetrixException{
        eventSvc.delete(id);
        return "redirect:/" + VIEW_PREFIX + "index";
    }
}