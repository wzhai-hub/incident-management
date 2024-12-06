package com.homework.incident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class IncidentApplication {
	private static final Logger logger = LoggerFactory.getLogger(IncidentApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(IncidentApplication.class);
		Environment env = app.run(args).getEnvironment();

		String port = env.getProperty("server.port", "8080");
		String host = env.getProperty("server.address", "localhost");

		logger.info("Application started successfully!");
		logger.info("Listening on {}:{}", host, port);
	}
}
