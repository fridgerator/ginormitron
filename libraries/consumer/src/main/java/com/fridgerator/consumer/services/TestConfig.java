package com.fridgerator.consumer.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
public class TestConfig {
    static final Logger logger = LoggerFactory.getLogger(TestConfig.class);

    public TestConfig () {
        logger.info("TestConfig constructor");
    }
}
 