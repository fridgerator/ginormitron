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
    private String cutomersTopic;

    CustomerGenerator (KafkaTemplate<String, Customer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void generateCustomers () throws InterruptedException {
        Faker faker = new Faker();

        while (true) {
            Thread.sleep(300);

            String name = faker.name().fullName();
            String address = faker.address().streetAddress();
            Customer customer = new Customer(name, address);

            logger.info("customer : {}, {}", name, address);

            try {
                kafkaTemplate.send(cutomersTopic, customer).get();
            } catch (Exception e) {
                logger.error("Error publishing : {}", e);
            }
        }
    }
}
