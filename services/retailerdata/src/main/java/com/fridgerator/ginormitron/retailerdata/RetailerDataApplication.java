package com.fridgerator.ginormitron.retailerdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fridgerator.ginormitron.retailerdata.generator.RetailerGenerator;

@SpringBootApplication
@EnableScheduling
public class RetailerDataApplication implements CommandLineRunner {

	@Autowired
	private RetailerGenerator generator;

	RetailerDataApplication(RetailerGenerator generator) {
		this.generator = generator;
	}

	public static void main(String[] args) {
		SpringApplication.run(RetailerDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {
		generator.generateRetailers();
	}
}
