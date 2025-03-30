package com.goga74.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class PlatformApplication
{
	private static final Logger logger = LogManager.getLogger(PlatformApplication.class);

	public static void main(String[] args)
	{
		logger.info("Application starting...");
		SpringApplication.run(PlatformApplication.class, args);
		logger.info("Application started.");
	}
}