package com.metrix.webportal.services;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.metrix.services.common.apis.TicketApi;
import com.metrix.services.common.domains.Tickets;
import com.metrix.services.common.domains.MetrixException;

@FeignClient(name = "TicketsApiServices", url = "${svc.url.ticket}")
public interface TicketsServices extends TicketApi {
    @Override
    @GetMapping("/index")
    public List<Tickets> index() throws MetrixException;
}