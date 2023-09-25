package com.fridgerator.ginormitron.transactiondata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fridgerator.ginormitron.transactiondata.generator.TransactionGenerator;
import com.fridgerator.ginormitron.transactiondata.repos.CustomerRepo;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	CustomerRepo customerRepo;

	@Autowired
	TransactionGenerator transactionGenerator;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		transactionGenerator.generateTransactions();
	}
}
