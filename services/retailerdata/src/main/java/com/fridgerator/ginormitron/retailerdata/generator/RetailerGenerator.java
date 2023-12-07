package com.fridgerator.ginormitron.retailerdata.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fridgerator.ginormitron.retailerdata.model.Retailer;
import com.github.javafaker.Faker;

@Service
public class RetailerGenerator {
    private static Logger logger = LogManager.getLogger(RetailerGenerator.class);

    @Autowired
    private KafkaTemplate<String, Retailer> kafkaTemplate;

    @Value("${kafka-topics.names.retailers}")
    private String retailersTopic;

    RetailerGenerator (KafkaTemplate<String, Retailer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void generateRetailers () throws InterruptedException {
        Faker faker = new Faker();

        while (true) {
            Thread.sleep(3000);

            Retailer retailer = new Retailer(
                faker.company().name(),
                faker.address().streetAddress(),
                faker.phoneNumber().phoneNumber(),
                faker.address().state(),
                faker.address().timeZone(),
                faker.address().zipCode(),
                faker.address().city()
            );

            logger.debug("retailer : {}", retailer);

            try {
                kafkaTemplate.send(retailersTopic, retailer);
                kafkaTemplate.flush();
                logger.debug("published");
            } catch (Exception e) {
                logger.error("Error publishing : {}", e);
            }
        }
    }
}
