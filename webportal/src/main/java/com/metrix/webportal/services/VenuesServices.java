package com.metrix.webportal.services;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.metrix.services.common.apis.VenueApi;
import com.metrix.services.common.domains.MetrixException;
import com.metrix.services.common.domains.Venues;

@FeignClient(name = "VenueApiService", url = "${svc.url.venue}")
public interface VenuesServices extends VenueApi {
    @Override
    @GetMapping("/index")
    public List<Venues> index() throws MetrixException;
}