package com.fridgerator.ginormitron.userdata.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fridgerator.ginormitron.userdata.model.User;
import com.github.javafaker.Faker;

@Service
public class UserGenerator {
    private static Logger logger = LogManager.getLogger(UserGenerator.class);

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    @Value("${kafka-topics.names.users}")
    private String usersTopic;

    UserGenerator (KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void generateUsers () throws InterruptedException {
        Faker faker = new Faker();

        while (true) {
            Thread.sleep(300);

            String name = faker.name().fullName();
            String address = faker.address().streetAddress();
            User user = new User(name, address);

            logger.info("user : {}, {}", name, address);

            try {
                kafkaTemplate.send(usersTopic, user).get();
            } catch (Exception e) {
                logger.error("there was an error : {}", e);
            }
        }
    }
}
