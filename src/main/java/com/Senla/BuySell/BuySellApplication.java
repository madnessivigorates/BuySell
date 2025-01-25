package com.Senla.BuySell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuySellApplication {
	private static final Logger logger = LogManager.getLogger(BuySellApplication.class);

	public static void main(String[] args) {
		logger.info("Приложение BuySell запускается...");
		SpringApplication.run(BuySellApplication.class, args);
		logger.info("Приложение BuySell успешно запущено.");
	}
}
