package com.metrix.services.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EntityScan("com.metrix.services.common.domains")
@EnableFeignClients(basePackages = {"com.metrix.services.ticket.services", "com.metrix.services.common.apis"})
public class TicketServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketServicesApplication.class, args);
	}

}
