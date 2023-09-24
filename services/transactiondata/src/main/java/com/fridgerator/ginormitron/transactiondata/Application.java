package com.fridgerator.ginormitron.transactiondata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fridgerator.ginormitron.transactiondata.repos.CustomerRepo;

@SpringBootApplication
public class Application {

	@Autowired
	CustomerRepo customerRepo;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
