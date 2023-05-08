package com.metrix.webportal.services;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.metrix.services.common.apis.EventsApi;
import com.metrix.services.common.domains.Events;
import com.metrix.services.common.domains.MetrixException;

@FeignClient(name = "EventsApiServices", url = "${svc.url.event}")
public interface EventsServices extends EventsApi {
    @Override
    @GetMapping("/index")
    public List<Events> index() throws MetrixException;
}