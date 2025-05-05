package com.nlw_connect.events;

import com.nlw_connect.events.model.Event;
import com.nlw_connect.events.repository.EventRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class EventsApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void shouldCreateEvent() {
		Event event = new Event();

		event.setTitle("Java summit 2025");
		event.setPrice(0.0);

		Assertions.assertNotNull(event);
	}

}
