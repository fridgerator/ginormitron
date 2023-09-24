package com.fridgerator.ginormitron.transactiondata.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fridgerator.ginormitron.transactiondata.entities.Retailer;
import com.fridgerator.ginormitron.transactiondata.repos.RetailerRepo;

@Service
public class RetailerConsumer {
    @Autowired
    RetailerRepo retailerRepo;

    static final Logger logger = LoggerFactory.getLogger(RetailerConsumer.class);

    @KafkaListener(topics = "${kafka-topics.names.retailers}", containerFactory = "retailerKafkaListenerContainerFactory")
    public void handleRetailer(Retailer retailer) {
        retailerRepo.save(retailer);
        logger.debug("retailer saved : {}", retailer);
    }
}
