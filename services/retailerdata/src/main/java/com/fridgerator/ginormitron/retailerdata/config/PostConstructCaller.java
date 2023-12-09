package com.fridgerator.ginormitron.retailerdata.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fridgerator.ginormitron.retailerdata.generator.RetailerGenerator;

import jakarta.annotation.PostConstruct;

@Component
public class PostConstructCaller {
    private static final Logger logger = LogManager.getLogger(PostConstructCaller.class);

    private RetailerGenerator generator;

    PostConstructCaller(RetailerGenerator generator) {
        this.generator = generator;
    }

    @PostConstruct
    public void init () throws InterruptedException {
        logger.info("starting generator");
        generator.generateRetailers();
    }
}
