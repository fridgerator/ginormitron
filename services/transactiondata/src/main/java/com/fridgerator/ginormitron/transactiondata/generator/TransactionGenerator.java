package com.fridgerator.ginormitron.transactiondata.generator;

import java.time.Instant;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fridgerator.ginormitron.transactiondata.entities.Customer;
import com.fridgerator.ginormitron.transactiondata.entities.Retailer;
import com.fridgerator.ginormitron.transactiondata.entities.Transaction;
import com.fridgerator.ginormitron.transactiondata.repos.CustomerRepo;
import com.fridgerator.ginormitron.transactiondata.repos.RetailerRepo;
import com.github.javafaker.Faker;

@Service
public class TransactionGenerator {
    private static Logger logger = LogManager.getLogger(TransactionGenerator.class);
    private static Boolean sufficientData = false;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private RetailerRepo retailerRepo;

    @Autowired
    private KafkaTemplate<String, Transaction> kafkaTemplate;

    @Value("${kafka-topics.names.transactions}")
    private String transactionsTopic;

    private Boolean checkSufficientData () throws InterruptedException {
        if (TransactionGenerator.sufficientData) return true;

        Thread.sleep(5000);

        long customersCount = customerRepo.count();
        long retailersCount = retailerRepo.count();

        logger.info("customers: {}, retailers: {}", customersCount, retailersCount);

        if (customersCount > 500 && retailersCount > 50) {
            TransactionGenerator.sufficientData = true;
        }

        return false;
    }

    public void generateTransactions () throws InterruptedException {
        Faker faker = new Faker();

        while (true) {
            if (!checkSufficientData()) continue;

            Thread.sleep(300);

            Retailer retailer = retailerRepo.getRandom().get(0);
            List<Customer> customers = customerRepo.getRandomSet();

            logger.info("retailer : {}", retailer);

            // TODO: transaction / batch create
            for (Customer customer : customers) {
                Transaction transaction = new Transaction(
                    customer.get_id(),
                    retailer.get_id(),
                    faker.commerce().productName(),
                    faker.commerce().price(),
                    Instant.now().toString()
                );

                try {
                    kafkaTemplate.send(transactionsTopic, transaction).get();
                    logger.info("published transaction: {}", transaction);
                } catch (Exception e) {
                    logger.error("Error publishing : {}", e);
                }
            }   
        }
    }
}
