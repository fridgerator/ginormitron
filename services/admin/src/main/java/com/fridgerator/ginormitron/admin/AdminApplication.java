package com.fridgerator.ginormitron.admin;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.scheduling.annotation.EnableScheduling;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
// @EnableScheduling
@EnableAutoConfiguration
@EnableAdminServer
public class AdminApplication {

	// @Autowired
	// private CustomerGenerator generator;

	// CustomerDataApplication(CustomerGenerator generator) {
	// 	this.generator = generator;
	// }

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	// @Override
	// public void run(String... args) throws InterruptedException {
	// 	generator.generateCustomers();
	// }
}
