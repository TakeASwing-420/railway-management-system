package com.railway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RailwayManagementSystemApplication {

	public static void main(String[] args) {
		// Set system property to prevent headless mode
		System.setProperty("java.awt.headless", "false");
		
		ConfigurableApplicationContext context = SpringApplication.run(RailwayManagementSystemApplication.class, args);
		
		// Keep the application running even if the GUI is closed
		context.registerShutdownHook();
	}

}
