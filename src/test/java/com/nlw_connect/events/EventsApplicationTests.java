package com.nlw_connect.events;

import com.nlw_connect.events.model.Events;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
		Events event = new Events();

		event.setTitle("Java summit 2025");
		event.setPrice(0);

		Assertions.assertNotNull(event);
	}

}
