package com.metrix.webportal.controllers;

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
import com.metrix.services.common.domains.MetrixException;
import com.metrix.services.common.domains.Venues;
import com.metrix.webportal.services.VenuesServices;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/venues")
public class VenuesController {
    
    private static final String VIEW_PREFIX = "venues/";

    @Autowired
    private VenuesServices venueSvc;

    @GetMapping({"","/","/index"})
    public String index(ModelMap m) throws MetrixException{
        m.addAttribute("allVenues", venueSvc.index());
        return VIEW_PREFIX + "index";
    }

    @GetMapping("/create")
    public String create(ModelMap m){
        m.addAttribute("newVenue", new Venues());
        return VIEW_PREFIX + "create";
    }

    @PostMapping("/create")
    public String create(ModelMap m, @Valid @ModelAttribute("newVenue") Venues newVenues, BindingResult result) throws MetrixException{
        if(result.hasErrors()){
            m.addAttribute("newVenue", newVenues);
            return VIEW_PREFIX + "create";
        }
        venueSvc.create(newVenues);
        return "redirect:/" + VIEW_PREFIX + "index";
    }

    @GetMapping("/update/{id}")
    public String update(ModelMap m, @PathVariable("id")Integer id) throws MetrixException{
        Optional<Venues> oVenue = venueSvc.retrieve(id);
        //if venue with id not found, throw exception, include redirect url to /venues/index in error page
        if(!oVenue.isPresent()){
            throw new MetrixException(-1, String.format("Venue with id {%d} not found!", id), "/" + VIEW_PREFIX + "index");
        }
        m.addAttribute("updVenue", oVenue.get());
        return VIEW_PREFIX + "update";
    }

    @PostMapping("/update/{id}")
    public String update(ModelMap m, @PathVariable("id")Integer id, @Valid @ModelAttribute("updVenue") Venues updVenues, BindingResult result) throws MetrixException{
        if(result.hasErrors()){
            m.addAttribute("updVenue", updVenues);
            return VIEW_PREFIX + "update";
        }
        venueSvc.update(id, updVenues);
        return "redirect:/" + VIEW_PREFIX + "index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Integer id) throws MetrixException{
        venueSvc.delete(id);
        return "redirect:/" + VIEW_PREFIX + "index";
    }  
}
