package com.fridgerator.ginormitron.transactiondata.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.fridgerator.ginormitron.transactiondata.entities.Customer;

public interface CustomerRepo extends MongoRepository<Customer, String> {
    public Optional<Customer> findById(String id);
    public long count();

    @Query("db.customer.aggregate([{$sample: { size: 10 }}])")
    public List<Customer> getRandomSet();
}
