package com.fridgerator.ginormitron.userdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fridgerator.ginormitron.userdata.generator.UserGenerator;

@SpringBootApplication
@EnableScheduling
public class UserDataApplication implements CommandLineRunner {

	@Autowired
	private UserGenerator generator;

	UserDataApplication(UserGenerator generator) {
		this.generator = generator;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws InterruptedException {
		generator.generateUsers();
	}
}
