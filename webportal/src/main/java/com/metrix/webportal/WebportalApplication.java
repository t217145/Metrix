package com.metrix.webportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.metrix.webportal.services", "com.metrix.services.common.apis"})
public class WebportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebportalApplication.class, args);
	}

}
