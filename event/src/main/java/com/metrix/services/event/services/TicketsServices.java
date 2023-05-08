package com.metrix.services.event.services;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.metrix.services.common.apis.TicketApi;
import com.metrix.services.common.domains.MetrixException;
import com.metrix.services.common.domains.Tickets;

@FeignClient(name = "TicketsApiService", url = "${svc.url.ticket}")
public interface TicketsServices extends TicketApi {
    @Override
    @GetMapping("/index")
    public List<Tickets> index() throws MetrixException;
}