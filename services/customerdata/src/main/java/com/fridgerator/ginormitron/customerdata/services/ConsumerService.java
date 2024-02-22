package com.fridgerator.ginormitron.customerdata.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fridgerator.ginormitron.customerdata.model.Customer;

@Service
public class ConsumerService {
    static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = "${kafka-topics.names.customers}", containerFactory = "customerKafkaListenerContainerFactory")
    void handle(Customer customer) {
        logger.info("Consumed customer: {}", customer);
    }
}
