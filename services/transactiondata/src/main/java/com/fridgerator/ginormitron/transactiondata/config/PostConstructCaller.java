package com.fridgerator.ginormitron.transactiondata.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fridgerator.ginormitron.transactiondata.generator.TransactionGenerator;

import jakarta.annotation.PostConstruct;

@Component
public class PostConstructCaller {
    private static final Logger logger = LogManager.getLogger(PostConstructCaller.class);

    private TransactionGenerator generator;

    PostConstructCaller(TransactionGenerator generator) {
        this.generator = generator;
    }

    @PostConstruct
    public void init() throws InterruptedException {
        logger.info("starting generator");
        generator.generateTransactions();
    }
}
