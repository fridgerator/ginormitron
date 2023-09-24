package com.fridgerator.ginormitron.transactiondata.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fridgerator.ginormitron.transactiondata.entities.Customer;
import com.fridgerator.ginormitron.transactiondata.repos.CustomerRepo;

@Service
public class CustomerConsumer {

    @Autowired
	CustomerRepo customerRepo;

    static final Logger logger = LoggerFactory.getLogger(CustomerConsumer.class);
    
    @KafkaListener(topics = "${kafka-topics.names.customers}", containerFactory = "customerKafkaListenerContainerFactory")
    public void handleCustomers(Customer customer) {
        customerRepo.save(customer);
        logger.debug("customer saved : {}", customer);
    }
}
