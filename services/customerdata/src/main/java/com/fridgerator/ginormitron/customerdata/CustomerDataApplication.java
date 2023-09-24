package com.fridgerator.ginormitron.customerdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fridgerator.ginormitron.customerdata.generator.CustomerGenerator;

@SpringBootApplication
@EnableScheduling
public class CustomerDataApplication implements CommandLineRunner {

	@Autowired
	private CustomerGenerator generator;

	CustomerDataApplication(CustomerGenerator generator) {
		this.generator = generator;
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {
		generator.generateCustomers();
	}
}
