package com.Senla.BuySell;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BuySellApplicationTest {

	@Test
	void contextLoads() {
		assertDoesNotThrow(() -> BuySellApplication.main(new String[]{}));
	}
}

