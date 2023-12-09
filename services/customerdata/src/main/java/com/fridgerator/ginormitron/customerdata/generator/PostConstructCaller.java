package com.fridgerator.ginormitron.customerdata.generator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class PostConstructCaller {
    private static final Logger logger = LogManager.getLogger(PostConstructCaller.class);

    private CustomerGenerator generator;

    PostConstructCaller(CustomerGenerator generator) {
        this.generator = generator;
    }

    @PostConstruct
    public void init() throws InterruptedException {
        logger.info("starting generator");
        generator.generateCustomers();
    }
}
