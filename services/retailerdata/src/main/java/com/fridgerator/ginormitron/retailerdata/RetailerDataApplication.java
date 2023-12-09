package com.fridgerator.ginormitron.retailerdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RetailerDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetailerDataApplication.class, args);
	}

}
