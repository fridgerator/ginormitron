package com.fridgerator.ginormitron.transactiondata.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fridgerator.ginormitron.transactiondata.entities.Retailer;

public interface RetailerRepo extends MongoRepository<Retailer, String> {
    public Optional<Retailer> findById(String id);
}
