package com.melita.NotificationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "spring.profiles.active=test")
@ExtendWith(SpringExtension.class)
class NotificationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
