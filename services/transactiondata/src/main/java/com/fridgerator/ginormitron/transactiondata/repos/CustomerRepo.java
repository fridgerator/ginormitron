package com.fridgerator.ginormitron.transactiondata.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fridgerator.ginormitron.transactiondata.entities.Customer;

public interface CustomerRepo extends MongoRepository<Customer, String> {
    public Optional<Customer> findById(String id);
}
