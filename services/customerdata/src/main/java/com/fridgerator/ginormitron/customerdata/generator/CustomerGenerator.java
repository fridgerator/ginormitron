package com.fridgerator.ginormitron.customerdata.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fridgerator.ginormitron.customerdata.model.Customer;
import com.github.javafaker.Faker;

@Service
public class CustomerGenerator {
    private static Logger logger = LogManager.getLogger(CustomerGenerator.class);

    @Autowired
    private KafkaTemplate<String, Customer> kafkaTemplate;

    @Value("${kafka-topics.names.customers}")
    private String customersTopic;

    @Async("threadPoolTaskExecutor")
    public void generateCustomers () throws InterruptedException {
        Faker faker = new Faker();

        while (true) {
            Thread.sleep(300 / 10);

            Customer customer = new Customer(
                faker.name().fullName(),
                faker.address().streetAddress(),
                faker.phoneNumber().phoneNumber(),
                faker.address().state(),
                faker.address().timeZone(),
                faker.address().zipCode(),
                faker.address().city()
            );

            logger.debug("customer : {}", customer);

            try {
                kafkaTemplate.send(customersTopic, customer);
                kafkaTemplate.flush();
                GeneratorCounter.incrementGeneratedCount();
            } catch (Exception e) {
                logger.error("Error publishing : {}", e);
            }
        }
    }
}
