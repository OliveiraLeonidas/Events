package com.nlw_connect.events;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@OpenAPIDefinition(
		info = @Info(
				title = "Events API Docs",
				version = "1.0.0",
				description = "Documentation API for Events management"
		)
)

@SpringBootApplication
public class EventsApplication {

	public static void main(String[] args) {

		SpringApplication.run(EventsApplication.class, args);
	}

}
